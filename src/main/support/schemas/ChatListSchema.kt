package support.schemas

import kotlinx.serialization.Serializable
import support.entities.Chat


@Serializable
class ChatListSchema(
    val schoolId: Int,
    val name: String,
) {
    companion object {
        fun fromChat(chat: Chat) = ChatListSchema(
            schoolId = chat.schoolId,
            name = chat.name,
        )
    }
}
