package support.clients

import support.di.UsersClient
import support.entities.User

class HttpUsersClient : UsersClient {
    override suspend fun getInfo(userId: Int): User {
        return User(id = userId, fullName = "Ivanov Ivan")
    }
}
