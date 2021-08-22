package support.schemas

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageSchema (
    val text: String,
    val files: List<FileSchema>,
)
