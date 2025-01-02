package com.haulcompliance.inspections.config.migrations

import io.tcds.orm.migrations.Migration

@Suppress("FunctionName")
fun migration_2025_01_01_115510_create_violations_table() = Migration(
    up = """
        CREATE TABLE `violations`
        (
            `inspection_id`           VARCHAR(255) NOT NULL,
            `convicted_of_dif_charge` BOOLEAN      NOT NULL,
            `code`                    VARCHAR(255),
            `description`             VARCHAR(255),
            `oos`                     BOOLEAN,
            `time_severity_weight`    INT,
            `basic`                   VARCHAR(255),
            `unit`                    VARCHAR(255),
        
            INDEX index_basic (basic),
            INDEX index_inspection_id (inspection_id)
        );
    """.trimIndent(),
    down = """
        DROP TABLE IF EXISTS `violations`
    """.trimIndent(),
)
