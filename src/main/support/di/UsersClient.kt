package support.di

import support.entities.User

interface UsersClient {
    suspend fun getInfo(userId: Int): User
}
