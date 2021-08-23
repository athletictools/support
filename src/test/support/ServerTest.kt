package support

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import support.di.SupportService
import support.entities.Author
import support.entities.Chat
import support.entities.Message
import support.entities.User
import support.schemas.ChatSchema
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

fun TestApplicationEngine.handleRequestWithUserId(
    method: HttpMethod,
    uri: String,
    userId: Int,
    setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall = handleRequest {
    this.uri = uri
    this.method = method
    addHeader("X-UserId", userId.toString())
    setup()
}

fun TestApplicationEngine.handleAdminRequest(
    method: HttpMethod,
    uri: String,
    userId: Int,
    setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall = handleRequestWithUserId(method, uri, userId) {
    addHeader("X-IsAdmin", "true")
    setup()
}

class ServerTest {
    private val supportUser = User(id = 2, fullName = "Support")
    private val clientUser = User(id = 3, fullName = "Client")
    private val message = Message(
        author = Author.SUPPORT,
        user = supportUser,
        text = "message test",
        files = emptyList(),
        created = Date(),
        updated = Date(),
        isReadBy = emptyList(),
    )
    private val chat = Chat(
        schoolId = 1,
        messages = listOf(message),
        participants = listOf(supportUser, clientUser)
    )

    @Test
    fun testGetAdminChats_OnlyAdminAllowed() {
        withTestApplication({ admin(service = mock()) }) {
            handleRequest(HttpMethod.Get, "/public/admin-get-chats?limit=10&offset=0").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
            handleRequestWithUserId(HttpMethod.Get, "/public/admin-get-chats?limit=10&offset=0", clientUser.id).apply {
                assertEquals(HttpStatusCode.Forbidden, response.status())
            }
        }
    }

    @Test
    fun testGetAdminChats() {
        val expectedContent = Json.encodeToString(
            mapOf("chats" to listOf(ChatSchema.fromChat(chat)))
        )
        val supportServiceMock = mock<SupportService> {
            on { getChats(10u, 0u) }.doReturn(listOf(chat))
        }

        withTestApplication({ admin(service = supportServiceMock) }) {
            handleAdminRequest(HttpMethod.Get, "/public/admin-get-chats?limit=10&offset=0", supportUser.id).apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(expectedContent, response.content)
            }
        }
    }

    @Test
    fun testGetAdminChats_limitRequiredAndMustBeValidNumber() {
        withTestApplication({ admin(service = mock()) }) {
            handleAdminRequest(HttpMethod.Get, "/public/admin-get-chats?offset=0", supportUser.id).apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("""{"error":"limit must be set"}""", response.content)
            }
            handleAdminRequest(HttpMethod.Get, "/public/admin-get-chats?limit=NaN", supportUser.id).apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("""{"error":"Invalid number format: 'NaN'"}""", response.content)
            }
        }
    }

    @Test
    fun testGetAdminChats_offsetRequiredAndMustBeValidNumber() {
        val expectedContent = """{"error":"offset must be set"}"""
        withTestApplication({ admin(service = mock()) }) {
            handleAdminRequest(HttpMethod.Get, "/public/admin-get-chats?limit=10", supportUser.id).apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals(expectedContent, response.content)
            }
            handleAdminRequest(HttpMethod.Get, "/public/admin-get-chats?limit=10&offset=NaN", supportUser.id).apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("""{"error":"Invalid number format: 'NaN'"}""", response.content)
            }
        }
    }
}
