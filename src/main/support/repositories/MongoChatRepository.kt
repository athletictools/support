package support.repositories

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import support.di.ChatRepository
import support.entities.Chat


class MongoChatRepository(private val db: CoroutineDatabase): ChatRepository {
    private val collection = db.getCollection<Chat>("chats")
    override suspend fun get(schoolId: Int): Chat? {
        println(collection.toString())
        return collection.findOne(Chat::schoolId eq schoolId)
    }

    override suspend fun save(chat: Chat) {
        collection.insertOne(chat)
    }
}
