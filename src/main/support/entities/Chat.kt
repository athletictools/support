package support.entities

data class Chat (
    val schoolId: Int,
    val name: String,
    val messages: List<Message>,
    val participants: List<User>,
)
