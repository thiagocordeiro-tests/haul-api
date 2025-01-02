package com.haulcompliance.inspections.config.migrations

import io.tcds.orm.migrations.Migration

@Suppress("FunctionName")
fun migration_2025_01_01_115237_create_inspection_vehicles_table() = Migration(
    up = """
        CREATE TABLE `inspection_vehicles`
        (
            `inspection_id` VARCHAR(255) NOT NULL,
            `vehicle_id`    VARCHAR(255) NOT NULL,
            `unit`          INT          NOT NULL,
            
            UNIQUE INDEX index_inspection_vehicle (inspection_id, vehicle_id),
            INDEX index_vehicle_id (vehicle_id)
        )
    """.trimIndent(),
    down = """
        DROP TABLE IF EXISTS `inspection_vehicles`
    """.trimIndent(),
)
