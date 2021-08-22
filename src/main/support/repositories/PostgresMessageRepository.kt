package support.repositories

import support.di.ChatRepository
import support.entities.Chat

class PostgresMessageRepository: ChatRepository {
    override fun getForSchool(schoolId: Int): Chat {
        TODO("Not yet implemented")
    }
}
