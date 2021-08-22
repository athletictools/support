package support.services

import support.di.ChatRepository
import support.di.SupportService
import support.entities.Author
import support.entities.Chat
import support.entities.File
import support.entities.Message


class ClientSupportService(val repository: ChatRepository): SupportService {
    override fun getChats(limit: UInt, offset: UInt): List<Chat> {
        TODO("Not yet implemented")
    }

    override fun getChat(schoolId: Int): Chat {
        TODO("Not yet implemented")
    }

    override fun getChatUnreadCount(schoolId: Int, userId: Int): UInt {
        TODO("Not yet implemented")
    }

    override fun sendMessage(schoolId: Int, author: Author, userId: Int, text: String, files: List<File>): Message {
        TODO("Not yet implemented")
    }
}
