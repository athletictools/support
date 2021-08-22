package support.schemas

import kotlinx.serialization.Serializable
import support.entities.User

@Serializable
data class UserSchema(
    val id: Int,
    val fullName: String,
) {
    companion object {
        fun fromUser(user: User) = UserSchema(id = user.id, fullName = user.fullName)
    }
}
