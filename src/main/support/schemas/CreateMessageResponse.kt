package support.schemas

import support.entities.File
import java.util.*

class CreateMessageResponse(
    val text: String,
    val files: List<File>,
    val created: Date,
    val updated: Date,
)
