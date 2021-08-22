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
import support.schemas.ChatSchema
import support.schemas.MessageSchema
import support.schemas.SendMessageSchema

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.public() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<IllegalArgumentException> {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message !!))
        }
    }
    val service: SupportService by di.instance()

    routing {
        route("/public") {
            get("/admin-get-chats") {
                getAdminChats(service)
            }
            get("/admin-get-chat/{schoolId}") {
                getAdminChat(service)
            }
            get("/get-chat/{schoolId}") {
                getAdminChat(service)
            }
            get("/chat/{schoolId}/send") {
                sendMessage(service, Author.CLIENT)
            }
            get("/chat/{schoolId}/admin-send") {
                sendMessage(service, Author.SUPPORT)
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
    call.respond(chats.map { ChatSchema.fromChat(it) })
}

suspend fun Handler.getAdminChat(service: SupportService) {
    val schoolId = call.parameters["schoolId"] !!.toInt()
    val chat = service.getChat(schoolId)
    call.respond(ChatSchema.fromChat(chat))
}

suspend fun Handler.sendMessage(service: SupportService, author: Author) {
    val schoolId = call.parameters["schoolId"] !!.toInt()
    val userId = call.request.headers["X-UserId"] !!
    val sendMessageSchema: SendMessageSchema = call.receive()
    val message = service.sendMessage(
        schoolId,
        author = author,
        userId = userId.toInt(),
        text = sendMessageSchema.text,
        files = sendMessageSchema.files.map { File(it.hash) }
    )
    call.respond(MessageSchema.fromMessage(message))
}
