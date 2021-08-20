package support

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import kotlinx.serialization.SerializationException

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<SerializationException> {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message !!))
        }
    }
    routing {
        route("/public") {
            get("/messages") {
                call.respond("Hello, world!")
            }
            post("/create-message") {
                call.respond("")
            }
        }
    }
}
