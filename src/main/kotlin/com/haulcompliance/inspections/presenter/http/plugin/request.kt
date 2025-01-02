package com.haulcompliance.inspections.presenter.http.plugin

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.presenter.InvalidValue
import com.haulcompliance.inspections.presenter.http.jsonMapper
import kotlinx.coroutines.runBlocking
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.PathMethod
import org.http4k.routing.bind
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.http4k.routing.RoutingHttpHandler as HttpHandler

/**
 * This plugin offer a few handy methods to make it easier to configure routes, consume request, and send responses
 * http4k is a lightweight http framework, a more robust solution could be made with ktor, micronaut or other http framework
 */
val logger: Logger = LoggerFactory.getLogger("HTTP")


fun Response.json(content: Any): Response = this
    .body(jsonMapper.writeValueAsString(content))

fun Response.error(message: String) = json(mapOf("message" to message))

fun List<Reference>.names(): String = joinToString(", ") { "`${it.fieldName}`" }

inline fun <reified T> Request.receive(): T {
    return try {
        jsonMapper.readValue(bodyString(), T::class.java)
    } catch (e: InvalidFormatException) {
        throw InvalidValue("Parameter ${e.path.names()} is invalid")
    } catch (e: MismatchedInputException) {
        throw InvalidValue("Parameter ${e.path.names()} is missing")
    } catch (e: JacksonException) {
        throw when (e.cause) {
            is InvalidValue -> e.cause!!
            else -> InvalidValue("Request body is invalid")
        }
    }
}

infix fun PathMethod.handle(action: suspend (Request) -> Response): HttpHandler {
    return to { request ->
        try {
            runBlocking { action(request) }
        } catch (e: InvalidValue) {
            Response(Status.BAD_REQUEST).error(e.message!!)
        } catch (e: EntityNotFound) {
            Response(Status.NOT_FOUND).error(e.message!!)
        } catch (e: Exception) {
            logger.error(e.message, e)

            Response(Status.INTERNAL_SERVER_ERROR).error("Something went wrong")
        }
    }
}

fun get(path: String, handler: suspend (Request) -> Response): HttpHandler = path bind Method.GET handle handler
fun post(path: String, handler: suspend (Request) -> Response): HttpHandler = path bind Method.POST handle handler
