package support

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import org.kodein.di.instance
import support.di.SupportService
import support.di.di
import support.entities.Author
import support.entities.File
import support.schemas.ChatDetailSchema
import support.schemas.ChatListSchema
import support.schemas.MessageSchema
import support.schemas.SendMessageSchema

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val srv: SupportService by di.instance()
    testableModule(srv)
}


fun Application.testableModule(service: SupportService) {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<IllegalArgumentException> {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message !!))
        }
    }
    install(CheckHeadersFeatures) {
        checkHeaders {
            if (call.request.headers["X-UserId"] == null) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        checkHeaders {
            if (call.request.headers["X-CompanyId"] == null) {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

    routing {
        route("/public") {
            get("/get-chat") {
                getChat(service)
            }
            post("/chat/send") {
                sendMessage(service)
            }
            route("/admin") {
                install(CheckHeadersFeatures) {
                    checkHeaders {
                        if (call.request.headers["X-IsAdmin"] != "true") {
                            call.respond(HttpStatusCode.Forbidden)
                        }
                    }
                }
                get("/get-chats") {
                    getAdminChats(service)
                }
                get("/get-chat/{schoolId}") {
                    getAdminChat(service)
                }
                post("/chat/{schoolId}/send") {
                    sendAdminMessage(service)
                }
            }
        }
    }
}

typealias Handler = PipelineContext<Unit, ApplicationCall>

suspend fun Handler.getAdminChats(service: SupportService) {
    val query = call.request.queryParameters
    val limit = query["limit"]?.toUInt() ?: throw IllegalArgumentException("limit must be set")
    val offset = query["offset"]?.toUInt() ?: throw IllegalArgumentException("offset must be set")
    val chats = service.getChats(limit, offset)
    call.respond(
        mapOf("chats" to chats.map { ChatListSchema.fromChat(it) })
    )
}

suspend fun Handler.getAdminChat(service: SupportService) {
    val schoolId = call.parameters["schoolId"] !!.toInt()
    val chat = service.getChat(schoolId)
    if (chat == null) {
        call.respond(HttpStatusCode.NotFound)
        return
    }
    call.respond(ChatDetailSchema.fromChat(chat))
}

suspend fun Handler.getChat(service: SupportService) {
    val schoolId = call.request.headers["X-CompanyId"] !!.toInt()
    val chat = service.getChat(schoolId)
    if (chat == null) {
        call.respond(HttpStatusCode.NotFound)
        return
    }
    call.respond(ChatDetailSchema.fromChat(chat))
}

suspend fun Handler.sendAdminMessage(service: SupportService) {
    val schoolId = call.parameters["schoolId"] !!.toInt()
    val userId = call.request.headers["X-UserId"] !!.toInt()
    val sendMessageSchema: SendMessageSchema = call.receive()
    val message = service.sendMessage(
        schoolId,
        author = Author.SUPPORT,
        userId = userId,
        text = sendMessageSchema.text,
        files = sendMessageSchema.files.map { File(it.hash) }
    )
    call.respond(MessageSchema.fromMessage(message))
}

suspend fun Handler.sendMessage(service: SupportService) {
    val schoolId = call.request.headers["X-CompanyId"] !!.toInt()
    val userId = call.request.headers["X-UserId"] !!.toInt()
    val sendMessageSchema: SendMessageSchema = call.receive()
    val message = service.sendMessage(
        schoolId,
        author = Author.CLIENT,
        userId = userId,
        text = sendMessageSchema.text,
        files = sendMessageSchema.files.map { File(it.hash) }
    )
    call.respond(MessageSchema.fromMessage(message))
}
