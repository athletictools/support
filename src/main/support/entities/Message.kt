package support.entities

import java.util.*

enum class Author {
    SUPPORT,
    CLIENT,
}

data class Message(
    val author: Author,
    val user: User,
    val text: String,
    val files: List<File>,
    val created: Date,
    val updated: Date,
    val isReadBy: List<User> = emptyList()
)
