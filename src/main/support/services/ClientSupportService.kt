package support.services

import support.di.ChatRepository
import support.di.SupportService
import support.entities.Author
import support.entities.Chat
import support.entities.File
import support.entities.Message


class ClientSupportService(
    private val repository: ChatRepository,
    ) : SupportService {
    override suspend fun getChats(limit: UInt, offset: UInt): List<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun getChat(schoolId: Int): Chat {
        val chat = repository.get(schoolId)
        if (chat != null) {
            return chat
        }
        val newChat = Chat(schoolId = schoolId, name = "unknown")
        repository.save(newChat)
        return newChat
    }

    override suspend fun getChatUnreadCount(schoolId: Int, userId: Int): UInt {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        schoolId: Int,
        author: Author,
        userId: Int,
        text: String,
        files: List<File>
    ): Message {
        TODO("Not yet implemented")
    }
}
