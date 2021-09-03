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
        val resultChat = runBlocking { ClientSupportService(repoMock).getChat(schoolId) }

        assertEquals(chat, resultChat)
    }

    @Test
    fun `if chat does not exist create new`() {
        val expectedChat = Chat(schoolId = schoolId, name = "unknown")
        val repoMock = mock<ChatRepository> {
            onBlocking { get(schoolId) }.doReturn(null)
            onBlocking { save(chat) }.doReturn(Unit)
        }

        val resultChat = runBlocking { ClientSupportService(repoMock).getChat(schoolId) }

        assertEquals(expectedChat, resultChat)
    }
}

