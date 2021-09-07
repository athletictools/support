package support.di

import support.entities.Chat
import support.entities.Message
import support.entities.User

interface ChatRepository {
    suspend fun get(schoolId: Int): Chat?
    suspend fun save(chat: Chat)
    suspend fun update(
        schoolId: Int,
        name: String? = null,
        messages: List<Message>? = null,
        participants: List<User>? = null
    )
}
