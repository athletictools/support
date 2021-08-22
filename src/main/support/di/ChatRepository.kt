package support.di

import support.entities.Chat

interface ChatRepository {
    fun getForSchool(schoolId: Int): Chat
}
