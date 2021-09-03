package support.di

import support.entities.Author
import support.entities.Chat
import support.entities.File
import support.entities.Message


interface SupportService {
    suspend fun getChats(limit: UInt, offset: UInt): List<Chat>
    suspend fun getChat(schoolId: Int): Chat?
    suspend fun getChatUnreadCount(schoolId: Int, userId: Int): UInt
    suspend fun sendMessage(schoolId: Int, author: Author, userId: Int, text: String, files: List<File>): Message
}
