package support.schemas

import kotlinx.serialization.Serializable

@Serializable
data class FileSchema(
    val hash: String
)
