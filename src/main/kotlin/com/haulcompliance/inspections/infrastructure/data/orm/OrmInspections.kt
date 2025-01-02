package com.haulcompliance.inspections.infrastructure.data.orm

import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.domain.EntryList
import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.InspectionFilter
import com.haulcompliance.inspections.domain.inspection.Inspections
import com.haulcompliance.inspections.infrastructure.data.orm.table.InspectionTable
import com.haulcompliance.inspections.infrastructure.data.orm.table.InspectionVehicleTable
import com.haulcompliance.inspections.infrastructure.data.orm.table.ViolationTable
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.*

class OrmInspections(
    private val chunkSize: Int,
    private val connection: Connection,
    private val inspectionTable: InspectionTable,
    private val inspectionVehicleTable: InspectionVehicleTable,
    private val violationTable: ViolationTable,
) : Inspections {
    override fun store(inspections: Iterable<Inspection>) {
        inspections
            .chunked(chunkSize)
            .forEach { chunk -> doStore(chunk) }
    }

    override fun loadById(id: String): Inspection {
        return inspectionTable.loadById(id) ?: throw EntityNotFound("Inspection with id <$id> not found")
    }

    override fun list(filter: InspectionFilter): EntryList<Inspection> {
        val where = emptyWhere().apply {
            filter.basic?.let {
                and("id IN (SELECT inspection_id FROM violations WHERE inspection_id = inspections.id AND basic LIKE ?)", param(it))
            }
            filter.status?.let {
                and(inspectionTable.status equalsTo it)
            }
        }

        val queryTotal = StringBuilder("SELECT count(*) as total FROM inspections").apply {
            if (where.params().isNotEmpty()) {
                append(" WHERE ${where.toStmt()}")
            }
        }.let {
            connection.read(it.toString(), where.params()).first()
        }

        return EntryList(
            total = queryTotal.value<String>("total")?.toIntOrNull() ?: 0,
            entries = inspectionTable.findByQuery(
                StringBuilder("SELECT * FROM inspections").apply {
                    if (where.params().isNotEmpty()) {
                        append(" WHERE ${where.toStmt()}")
                    }

                    append(" ORDER BY ${filter.orderBy.value} ${filter.order.name} LIMIT ? OFFSET ?")
                }.toString(),
                *where.params().toTypedArray(),
                param(filter.entriesPerPage),
                param((filter.page - 1) * filter.entriesPerPage),
            ).toList(),
        )
    }

    private fun doStore(inspections: List<Inspection>) {
        val inspectionVehicleModels = mutableListOf<InspectionVehicleTable.Model>()
        val violationModels = mutableListOf<ViolationTable.Model>()

        inspections.forEach { inspection ->
            inspectionVehicleModels.addAll(
                inspection.vehicles.map { inspected -> InspectionVehicleTable.Model(inspection.id, inspected.vehicle.id, inspected.unit) },
            )

            violationModels.addAll(
                inspection.violations.map { violation -> ViolationTable.Model(inspection.id, violation) },
            )
        }

        if (inspections.isEmpty()) {
            return;
        }

        inspectionTable.bulkInsertIgnore(inspections)
        inspectionVehicleTable.bulkInsertIgnore(inspectionVehicleModels)

        if (violationModels.isNotEmpty()) {
            violationTable.bulkInsertIgnore(violationModels)
        }
    }
}
