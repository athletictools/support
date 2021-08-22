package support.schemas

import kotlinx.serialization.Serializable
import support.entities.Chat


@Serializable
class ChatSchema(
    val schoolId: Int,
    val messages: List<MessageSchema>,
    val participants: List<UserSchema>,
) {
    companion object {
        fun fromChat(chat: Chat) = ChatSchema(
            schoolId = chat.schoolId,
            messages = chat.messages.map { MessageSchema.fromMessage(it) },
            participants = chat.participants.map { UserSchema.fromUser(it) }
        )
    }
}
