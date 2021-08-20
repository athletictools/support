package support.entities

import java.util.*

data class Message(
    val schoolId: Int,
    val authorId: Int?,
    val text: String,
    val files: List<File>,
    val created: Date,
    val updated: Date,
)
