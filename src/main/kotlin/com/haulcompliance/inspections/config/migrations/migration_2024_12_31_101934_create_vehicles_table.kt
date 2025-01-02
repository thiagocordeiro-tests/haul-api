package com.haulcompliance.inspections.config.migrations

import io.tcds.orm.migrations.Migration

@Suppress("FunctionName")
fun migration_2024_12_31_101934_create_vehicles_table() = Migration(
    up = """
        CREATE TABLE `vehicles`
        (
            `id`             VARCHAR(255) PRIMARY KEY,
            `type`           VARCHAR(255) NOT NULL,
            `license_number` VARCHAR(255) NOT NULL,
            `license_state`  VARCHAR(255) NOT NULL
        );
    """.trimIndent(),
    down = """
        DROP TABLE IF EXISTS `vehicles`
    """.trimIndent(),
)
