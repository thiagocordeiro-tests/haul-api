package com.haulcompliance.inspections.config

import com.haulcompliance.inspections.config.migrations.*
import io.tcds.orm.connection.Connection
import io.tcds.orm.migrations.MigrationRunner
import org.slf4j.Logger

fun runMigrations(connection: Connection, logger: Logger) {
    val migrations = listOf(
        ::migration_2024_12_31_101934_create_vehicles_table,
        ::migration_2024_12_31_114812_create_inspections_table,
        ::migration_2025_01_01_115237_create_inspection_vehicles_table,
        ::migration_2025_01_01_115510_create_violations_table,
        ::migration_2025_01_01_143498_create_crashes_table,
    )

    try {
        MigrationRunner(connection).runMigrations(migrations) { message ->
            logger.info(message)
        }
    } catch (e: Exception) {
        logger.error("Failed to run migrations", e)
    }
}
