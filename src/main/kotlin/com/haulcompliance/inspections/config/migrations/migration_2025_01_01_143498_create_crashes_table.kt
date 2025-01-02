package com.haulcompliance.inspections.config.migrations

import io.tcds.orm.migrations.Migration

@Suppress("FunctionName")
fun migration_2025_01_01_143498_create_crashes_table() = Migration(
    up = """
        CREATE TABLE `crashes`
        (
            `id`                     VARCHAR(255) PRIMARY KEY,
            `vehicle_id`             VARCHAR(255) NOT NULL,
            `reported_date`          DATETIME(6)  NOT NULL,
            `state`                  VARCHAR(255) NOT NULL,
            `fatalities`             INT          NOT NULL,
            `injuries`               INT          NOT NULL,
            `tow_away`               BOOLEAN      NOT NULL,
            `hazmat_released`        BOOLEAN      NOT NULL,
            `severity_weight`        INT          NOT NULL,
            `time_weight`            INT          NOT NULL,
            `severity_time_weight`   INT          NOT NULL,
            `trafficway`             VARCHAR(255) NOT NULL,
            `access_control`         VARCHAR(255) NOT NULL,
            `road_surface_condition` VARCHAR(255) NOT NULL,
            `light_condition`        VARCHAR(255) NOT NULL,
            `weather_condition`      VARCHAR(255) NOT NULL,
            `not_preventable_det`    BOOLEAN      NOT NULL,
            `imported_at`            DATETIME(6)  NOT NULL,

            INDEX index_imported_at (imported_at),
            INDEX vehicle_id (vehicle_id)
        );
    """.trimIndent(),
    down = """
        DROP TABLE IF EXISTS `crashes`
    """.trimIndent(),
)
