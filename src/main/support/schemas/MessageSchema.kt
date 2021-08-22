package support.schemas

import kotlinx.serialization.Serializable
import support.entities.Author
import support.entities.Message
import support.entities.User
import java.util.*

@Serializable
class MessageSchema(
    val author: Author,
    val user: UserSchema,
    val text: String,
    val files: List<FileSchema>,
    @Serializable(with = DateSerializer::class)
    val created: Date,
    @Serializable(with = DateSerializer::class)
    val updated: Date,
) {
    companion object {
        fun fromMessage(message: Message) = with(message) {
            MessageSchema(
                author = author,
                user = UserSchema.fromUser(user),
                text = text,
                files = files.map { FileSchema.fromFile(it) },
                created = created,
                updated = updated,
            )
        }
    }
}
