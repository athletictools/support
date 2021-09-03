package support.di

import support.entities.Chat

interface ChatRepository {
    suspend fun get(schoolId: Int): Chat?
    suspend fun save(chat: Chat)
}
