package support.schemas

import kotlinx.serialization.Serializable
import support.entities.Chat


@Serializable
class ChatSchema(
    val schoolId: Int,
    val name: String,
    val messages: List<MessageSchema>,
    val participants: List<UserSchema>,
) {
    companion object {
        fun fromChat(chat: Chat) = ChatSchema(
            schoolId = chat.schoolId,
            name = chat.name,
            messages = chat.messages.map { MessageSchema.fromMessage(it) },
            participants = chat.participants.map { UserSchema.fromUser(it) }
        )
    }
}
