package com.haulcompliance.inspections.presenter.plugin

import com.haulcompliance.inspections.domain.EntityNotFound
import com.haulcompliance.inspections.presenter.InvalidValue
import com.haulcompliance.inspections.presenter.http.plugin.get
import com.haulcompliance.inspections.presenter.http.plugin.json
import com.haulcompliance.inspections.presenter.http.plugin.post
import com.haulcompliance.inspections.presenter.http.plugin.receive
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.routes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ErrorHandlingTest {
    data class Foo(val foo: String, val bar: Int)

    private val routes = routes(
        get("/invalid-value") {
            throw InvalidValue("Foo is invalid")
        },
        post("/invalid-payload") {
            Response(Status.ACCEPTED)
                .json(it.receive<Foo>())
        },
        get("/entity-not-found-error") {
            throw EntityNotFound("Bar not found")
        },
        get("/unhandled-error") {
            throw Exception("You shall not pass")
        },
    )

    @Test
    fun `when the request handler throws an invalid value exception then respond bad request`() {
        val request = Request(Method.GET, "/invalid-value")

        val response = routes(request)

        Assertions.assertEquals(Status.BAD_REQUEST, response.status)
        Assertions.assertEquals("""{"message":"Foo is invalid"}""", response.body.toString())
    }

    @Test
    fun `when the request body does not contain the required information then respond bad request`() {
        val request = Request(Method.POST, "/invalid-payload").body("{}")

        val response = routes(request)

        Assertions.assertEquals(Status.BAD_REQUEST, response.status)
        Assertions.assertEquals("""{"message":"Parameter `foo` is missing"}""", response.body.toString())
    }

    @Test
    fun `when the request body contains invalid data then respond bad request`() {
        val request = Request(Method.POST, "/invalid-payload").body("""{"foo":"Foo","bar":"xxx"}""")

        val response = routes(request)

        Assertions.assertEquals(Status.BAD_REQUEST, response.status)
        Assertions.assertEquals("""{"message":"Parameter `bar` is invalid"}""", response.body.toString())
    }

    @Test
    fun `when the request handler throws an entity not found exception then respond not found`() {
        val request = Request(Method.GET, "/entity-not-found-error")

        val response = routes(request)

        Assertions.assertEquals(Status.NOT_FOUND, response.status)
        Assertions.assertEquals("""{"message":"Bar not found"}""", response.body.toString())
    }

    @Test
    fun `when the request handler throws an unhandled exception then respond internal server error with a generic message`() {
        val request = Request(Method.GET, "/unhandled-error")

        val response = routes(request)

        Assertions.assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
        Assertions.assertEquals("""{"message":"Something went wrong"}""", response.body.toString())
    }
}
