package com.haulcompliance.inspections.config.migrations

import io.tcds.orm.migrations.Migration

@Suppress("FunctionName")
fun migration_2024_12_31_114812_create_inspections_table() = Migration(
    up = """
        CREATE TABLE `inspections`
        (
            `id`                           VARCHAR(255) PRIMARY KEY,
            `state`                        VARCHAR(255) NOT NULL,
            `level`                        INT          NOT NULL,
            `time_weight`                  INT          NOT NULL,
            `placarable_hazmat_inspection` BOOLEAN      NOT NULL,
            `hazmat_inspection`            BOOLEAN      NOT NULL,
            `inspected_at`                 DATETIME(6)  NOT NULL,
            `status`                       VARCHAR(255) NOT NULL,
            `imported_at`                  DATETIME(6)  NOT NULL,

            INDEX index_status (status),
            INDEX index_inspected_at (inspected_at),
            INDEX index_imported_at (imported_at)
        )
    """.trimIndent(),
    down = """
        DROP TABLE IF EXISTS `inspections`
    """.trimIndent(),
)
