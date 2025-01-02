package com.haulcompliance.inspections.presenter.http

import com.haulcompliance.inspections.application.inspection.get.GetInspectionsQuery
import com.haulcompliance.inspections.application.inspection.import.ImportInspectionReportCommand
import com.haulcompliance.inspections.application.inspection.list.ListInspectionsQuery
import com.haulcompliance.inspections.domain.inspection.Inspection
import com.haulcompliance.inspections.domain.inspection.InspectionFilter
import com.haulcompliance.inspections.presenter.InvalidValue
import com.haulcompliance.inspections.presenter.http.plugin.get
import com.haulcompliance.inspections.presenter.http.plugin.json
import com.haulcompliance.inspections.presenter.http.plugin.post
import org.http4k.core.*
import org.http4k.filter.AllowAll
import org.http4k.filter.CorsPolicy
import org.http4k.filter.OriginPolicy
import org.http4k.filter.ServerFilters
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.path
import org.http4k.routing.routes
import java.io.ByteArrayInputStream

fun Request.multipartFileContent(name: String): String {
    return MultipartFormBody
        .from(this)
        .file(name)?.content
        ?.let {
            if (it is ByteArrayInputStream) it.reset()
            String(it.readBytes())
        }
        ?: throw InvalidValue("Missing file")
}

fun Request.param(key: String, default: String? = null) = query(key) ?: path(key) ?: default
fun Request.string(key: String, default: String? = null) = param(key, default)
fun Request.int(key: String, default: String? = null) = param(key, default)?.toIntOrNull()
inline fun <reified T : Enum<T>> Request.enum(key: String, default: String? = null): T? = param(key, default)?.let { enumValueOf<T>(it) }

fun configureRoutes(): RoutingHttpHandler {
    val import = Container.importInspectionsCommandHandler
    val list = Container.listInspectionsQueryHandler
    val get = Container.getInspectionQueryHandler

    return routes(
        post("/inspections/reports") { request ->
            val command = ImportInspectionReportCommand(content = request.multipartFileContent("report"))
            val response = import.handle(command)

            Response(Status.ACCEPTED).json(response)
        },
        get("/inspections") { request ->
            val query = ListInspectionsQuery(
                filter = InspectionFilter(
                    basic = request.string("basic"),
                    status = request.enum<Inspection.Status>("status"),
                    page = request.int("page") ?: 1,
                    entriesPerPage = request.int("entriesPerPage") ?: 10,
                    order = request.enum<InspectionFilter.Order>(
                        key = "order",
                        default = InspectionFilter.Order.ASC.name,
                    ) ?: throw InvalidValue("Order is invalid"),
                    orderBy = request.enum<InspectionFilter.OrderBy>(
                        key = "orderBy",
                        default = InspectionFilter.OrderBy.InspectionNumber.name,
                    ) ?: throw InvalidValue("Order by is invalid"),
                ),
            )
            val response = list.handle(query)

            Response(Status.OK).json(response)
        },
        get("/inspections/{id}") { request ->
            val query = GetInspectionsQuery(
                id = request.string("id") ?: throw InvalidValue("Inspection id is invalid"),
            )
            val response = get.handle(query)

            Response(Status.OK).json(response)
        },
    ).withFilter(
        ServerFilters.Cors(
            CorsPolicy(
                originPolicy = OriginPolicy.AllowAll(),
                headers = listOf("*"),
                methods = Method.entries,
                credentials = true,
            ),
        ),
    )
}
