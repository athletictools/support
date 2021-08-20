package support.schemas

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequest (
    val text: String,
    val files: List<FileSchema>,
)
