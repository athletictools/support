package services

import org.mockito.kotlin.*
import support.di.ChatRepository
import support.entities.Author
import support.entities.Chat
import support.entities.Message
import support.entities.User
import support.services.ClientSupportService
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import support.di.Now
import support.di.UsersClient

class TestClientSupportService {
    private val schoolId = 1
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
        name = "School's chat",
        messages = listOf(message),
        participants = listOf(supportUser, clientUser)
    )

    @Test
    fun `if chat exist return it`() {
        val repoMock = mock<ChatRepository> {
            onBlocking { get(schoolId) }.doReturn(chat)
        }
        val nowMock = Now { throw Exception() }
        val usersClientMock = mock<UsersClient>()

        val resultChat = runBlocking { ClientSupportService(nowMock, repoMock, usersClientMock).getChat(schoolId) }

        assertEquals(chat, resultChat)
    }

    @Test
    fun `if chat does not exist create new`() {
        val expectedChat = Chat(schoolId = schoolId, name = "unknown")
        val repoMock = mock<ChatRepository> {
            onBlocking { get(schoolId) }.doReturn(null)
            onBlocking { save(chat) }.doReturn(Unit)
        }
        val nowMock = Now { throw Exception() }
        val usersClientMock = mock<UsersClient>()

        val resultChat = runBlocking { ClientSupportService(nowMock, repoMock, usersClientMock).getChat(schoolId) }

        assertEquals(expectedChat, resultChat)
    }

    @Test
    fun `send message from client`() {
        val now = Date()
        val expectedMessage = Message(
            Author.CLIENT,
            user = clientUser,
            text = "msg text",
            files = emptyList(),
            created = now,
            updated = now,
            isReadBy = emptyList()
        )
        val emptyChat = Chat(schoolId = schoolId, name = "new school")
        val updatedChat = emptyChat.copy(messages = listOf(expectedMessage))

        val repoMock = mock<ChatRepository> {
            onBlocking { get(schoolId) }.doReturn(emptyChat)
            onBlocking { save(updatedChat) }.doReturn(Unit)
        }
        val nowMock = Now { now }
        val usersClientMock = mock<UsersClient> {
            onBlocking { getInfo(clientUser.id) }.doReturn(clientUser)
        }

        val message = runBlocking {
            ClientSupportService(nowMock, repoMock, usersClientMock).sendMessage(
                schoolId,
                Author.CLIENT,
                userId = clientUser.id,
                text = "msg text",
                files = emptyList(),
            )
        }

        assertEquals(expectedMessage, message)
    }
}
