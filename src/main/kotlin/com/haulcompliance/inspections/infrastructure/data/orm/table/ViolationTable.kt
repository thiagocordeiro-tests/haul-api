package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.inspection.violation.Violation
import io.tcds.orm.OrmResultSet
import io.tcds.orm.Table
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.bool
import io.tcds.orm.extension.integerNullable
import io.tcds.orm.extension.varchar
import io.tcds.orm.extension.varcharNullable

@Suppress("MemberVisibilityCanBePrivate")
class ViolationTable(
    connection: Connection,
) : Table<ViolationTable.Model>(
    connection,
    "violations",
) {
    data class Model(val inspectionId: String, val violation: Violation)

    val inspectionId = varchar("inspection_id") { it.inspectionId }
    val convictedOfDifCharge = bool("convicted_of_dif_charge") { it.violation.convictedOfDifCharge }
    val code = varcharNullable("code") { it.violation.code }
    val description = varcharNullable("description") { it.violation.description }
    val oos = bool("oos") { it.violation.oos }
    val timeSeverityWeight = integerNullable("time_severity_weight") { it.violation.timeSeverityWeight }
    val basic = varcharNullable("basic") { it.violation.basic }
    val unit = integerNullable("unit") { it.violation.unit }

    override fun entry(row: OrmResultSet): Model = Model(
        inspectionId = row.get(inspectionId),
        violation = Violation(
            convictedOfDifCharge = row.get(convictedOfDifCharge),
            code = row.nullable(code),
            description = row.nullable(description),
            oos = row.get(oos),
            timeSeverityWeight = row.nullable(timeSeverityWeight),
            basic = row.nullable(basic),
            unit = row.nullable(unit),
        ),
    )
}
