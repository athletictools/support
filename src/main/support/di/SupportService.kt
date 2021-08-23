package support.di

import support.entities.Author
import support.entities.Chat
import support.entities.File
import support.entities.Message


interface SupportService {
    fun getChats(limit: UInt, offset: UInt): List<Chat>
    fun getChat(schoolId: Int): Chat?
    fun getChatUnreadCount(schoolId: Int, userId: Int): UInt
    fun sendMessage(schoolId: Int, author: Author, userId: Int, text: String, files: List<File>): Message
}
