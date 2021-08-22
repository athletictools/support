package support.schemas

import kotlinx.serialization.Serializable

@Serializable
data class UnreadCountSchema (
    val unread: UInt,
)
