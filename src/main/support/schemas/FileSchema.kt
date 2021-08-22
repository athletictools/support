package support.schemas

import kotlinx.serialization.Serializable
import support.entities.File

@Serializable
data class FileSchema(
    val hash: String
) {
    companion object {
        fun fromFile(file: File) = FileSchema(hash = file.hash)
    }
}
