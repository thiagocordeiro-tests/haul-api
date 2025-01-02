package com.haulcompliance.inspections.infrastructure.data.orm.table

import com.haulcompliance.inspections.domain.vehicle.Vehicles
import com.haulcompliance.inspections.domain.vehicle.crash.Crash
import io.tcds.orm.EntityTable
import io.tcds.orm.OrmResultSet
import io.tcds.orm.connection.Connection
import io.tcds.orm.extension.*

@Suppress("MemberVisibilityCanBePrivate")
class CrashTable(
    connection: Connection,
    private val vehicles: Vehicles,
) : EntityTable<Crash, String>(
    connection,
    "crashes",
) {
    override val id = varchar("id") { it.id }
    val reportedDate = datetime("reported_date") { it.reportedDate.toInstant() }
    val state = varchar("state") { it.state }
    val fatalities = integer("fatalities") { it.fatalities }
    val injuries = integer("injuries") { it.injuries }
    val towAway = bool("tow_away") { it.towAway }
    val hazmatReleased = bool("hazmat_released") { it.hazmatReleased }
    val severityWeight = integer("severity_weight") { it.severityWeight }
    val timeWeight = integer("time_weight") { it.timeWeight }
    val severityTimeWeight = integer("severity_time_weight") { it.severityTimeWeight }
    val trafficway = varchar("trafficway") { it.trafficway }
    val accessControl = varchar("access_control") { it.accessControl }
    val roadSurfaceCondition = varchar("road_surface_condition") { it.roadSurfaceCondition }
    val lightCondition = varchar("light_condition") { it.lightCondition }
    val weatherCondition = varchar("weather_condition") { it.weatherCondition }
    val notPreventableDet = bool("not_preventable_det") { it.notPreventableDet }
    val vehicleId = varchar("vehicle_id") { it.vehicle.id }
    val importedAt = datetime("imported_at") { it.importedAt.toInstant() }

    override fun entry(row: OrmResultSet): Crash = Crash(
        id = row.get(id),
        reportedDate = row.get(reportedDate).toLocalDate(),
        state = row.get(state),
        fatalities = row.get(fatalities),
        injuries = row.get(injuries),
        towAway = row.get(towAway),
        hazmatReleased = row.get(hazmatReleased),
        severityWeight = row.get(severityWeight),
        timeWeight = row.get(timeWeight),
        severityTimeWeight = row.get(severityTimeWeight),
        trafficway = row.get(trafficway),
        accessControl = row.get(accessControl),
        roadSurfaceCondition = row.get(roadSurfaceCondition),
        lightCondition = row.get(lightCondition),
        weatherCondition = row.get(weatherCondition),
        notPreventableDet = row.get(notPreventableDet),
        importedAt = row.get(importedAt).toLocalDateTime(),
        vehicle = lazy { vehicles.loadById(row.get(vehicleId)) },
    )
}
