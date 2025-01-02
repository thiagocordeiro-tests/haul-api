package com.haulcompliance.inspections.infrastructure.data.api.fmcsa

import com.haulcompliance.inspections.domain.inspection.ParsedReport
import com.haulcompliance.inspections.presenter.http.xmlMapper
import com.haulcompliance.inspections.testing.fixtures.CrashSpawner
import com.haulcompliance.inspections.testing.fixtures.InspectionSpawner
import com.haulcompliance.inspections.testing.freeze
import io.tcds.orm.testing.assertObjects
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class FmcsaXmlReportParserTest {
    private val parser = FmcsaXmlReportParser(xmlMapper)

    @Test
    fun `given a xml inspection content then parse into domain inspections`() = freeze {
        @Language("xml") val content = """
            <carrierData>
                <inspections>
                    <inspection inspection_date="2023-08-25"
                                report_state="TX"
                                report_number="id-truck-tractor-inspection"
                                level="1"
                                time_weight="1"
                                Placarable_HM_Veh_Insp="No"
                                HM_inspection="No">
                        <vehicles>
                            <vehicle unit="1"
                                 vehicle_id_number="TC12345"
                                 unit_type="Truck Tractor"
                                 license_state="NY"
                                 license_number="TRUCK-C12345"/>
                            <vehicle unit="2"
                                 vehicle_id_number="TK45678"
                                 license_state="CA"
                                 unit_type="Semi-Trailer"
                                 license_number="TRAILER-K12345"/>
                        </vehicles>
                        <violations>
                            <violation code="393.9T"
                                       description="Inoperable tail lamp"
                                       oos="N"
                                       time_severity_weight="6"
                                       BASIC="Vehicle Maint."
                                       unit="1"
                                       convicted_of_dif_charge="Y"/>
                            <violation code="393.45"
                                       description="Brake tubing and hose adequacy"
                                       oos="Y"
                                       time_severity_weight="6"
                                       BASIC="Vehicle Maint."
                                       unit="1"
                                       convicted_of_dif_charge="Y"/>
                        </violations>
                    </inspection>
                    <inspection inspection_date="2023-10-01"
                                report_state="PA"
                                report_number="id-straight-truck-inspection"
                                level="1"
                                time_weight="1"
                                Placarable_HM_Veh_Insp="No"
                                HM_inspection="No">
                        <vehicles>
                            <vehicle unit="1"
                                     vehicle_id_number="ST98765"
                                     unit_type="Straight Truck"
                                     license_state="PA"
                                     license_number="STRAIGHT-T981234"/>
                        </vehicles>
                        <violations>
                            <violation code="393.45"
                                       description="Brake tubing and hose adequacy"
                                       oos="Y"
                                       time_severity_weight="6"
                                       BASIC="Vehicle Maint."
                                       unit="1"
                                       convicted_of_dif_charge="Y"/>
                        </violations>
                    </inspection>
                </inspections>
                <crashes>
                    <crash report_date="2022-11-29"
                           report_state="WA"
                           report_number="id-snow-crash"
                           fatalities="0"
                           injuries="0"
                           tow_away="Y"
                           hazmat_released="N"
                           severity_weight="1"
                           time_weight="1"
                           severity_time_weight="1"
                           trafficway="Two-Way Trafficway, Divided, Positive Barrier"
                           access_control="No Control"
                           road_surface_condition="Wet"
                           light_condition="Dark - Not Lighted"
                           weather_condition="Snow"
                           not_preventable_det="Y">
                        <vehicle vehicle_id_number="TK45678"
                                 license_state="CA"
                                 unit_type="Semi-Trailer"
                                 license_number="TRAILER-K12345"/>
                    </crash>
                    <crash report_date="2022-12-09"
                           report_state="DE"
                           report_number="id-rain-crash"
                           fatalities="0"
                           injuries="0"
                           tow_away="Y"
                           severity_weight="1"
                           time_weight="1"
                           severity_time_weight="1"
                           trafficway="Two-Way Trafficway, Not Divided"
                           access_control="No Control"
                           road_surface_condition="Wet"
                           light_condition="Dawn"
                           weather_condition="Rain"
                           not_preventable_det="Y">
                        <vehicle vehicle_id_number="TK45678"
                                 license_state="CA"
                                 unit_type="Semi-Trailer"
                                 license_number="TRAILER-K12345"/>
                    </crash>
                </crashes>
            </carrierData>
        """.trimIndent()

        val parsed = parser.parse(content)

        assertObjects(
            ParsedReport(
                inspections = listOf(
                    InspectionSpawner.truckTractor(),
                    InspectionSpawner.straightTruck(),
                ),
                crashes = listOf(
                    CrashSpawner.snowCrash(),
                    CrashSpawner.sunLightCrash(),
                ),
            ),
            parsed,
        )
    }
}
