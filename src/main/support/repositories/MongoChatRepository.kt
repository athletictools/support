package support.repositories

import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import support.di.ChatRepository
import support.entities.Chat
import support.entities.Message
import support.entities.User


class MongoChatRepository(db: CoroutineDatabase): ChatRepository {
    private val collection = db.getCollection<Chat>("chats")

    override suspend fun list(limit: UInt, offset: UInt): List<Chat> {
        return collection.find().limit(limit.toInt()).skip(offset.toInt()).toList()
    }

    override suspend fun get(schoolId: Int): Chat? {
        return collection.findOne(Chat::schoolId eq schoolId)
    }

    override suspend fun save(chat: Chat) {
        collection.insertOne(chat)
    }

    override suspend fun update(schoolId: Int, name: String?, messages: List<Message>?, participants: List<User>?) {
        val updates = listOfNotNull(
            if (name != null) setValue(Chat::name, name) else null,
            if (messages != null) setValue(Chat::messages, messages) else null,
            if (participants != null) setValue(Chat::participants, participants) else null,
        )

        collection.updateOne(Chat::schoolId eq schoolId, combine(updates))
    }
}
