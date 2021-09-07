package support.services

import support.di.ChatRepository
import support.di.Now
import support.di.SupportService
import support.di.UsersClient
import support.entities.Author
import support.entities.Chat
import support.entities.File
import support.entities.Message

class ClientSupportService(
    private val now: Now,
    private val repository: ChatRepository,
    private val usersClient: UsersClient,
) : SupportService {
    override suspend fun getChats(limit: UInt, offset: UInt): List<Chat> {
        return repository.list(limit, offset)
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

    override suspend fun sendMessage(
        schoolId: Int,
        author: Author,
        userId: Int,
        text: String,
        files: List<File>
    ): Message {
        val chat = getChat(schoolId)
        val newParticipant = userId !in chat.participants.map { it.id }
        val user = if (newParticipant) {
            usersClient.getInfo(userId)
        } else {
            chat.participants.first { it.id == userId }
        }
        val message = Message(
            Author.CLIENT,
            user = user,
            text = text,
            files = files,
            created = now.get(),
            updated = now.get(),
        )
        repository.update(
            schoolId,
            messages = chat.messages + message,
            participants = if (newParticipant) chat.participants + user else null,
        )
        return message
    }
}
