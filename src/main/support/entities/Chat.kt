package support.entities

data class Chat(
    val schoolId: Int,
    val name: String,
    val messages: List<Message> = emptyList(),
    val participants: List<User> = emptyList(),
)
