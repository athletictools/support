package clients
import kotlinx.coroutines.runBlocking
import support.clients.HttpUsersClient
import support.entities.User
import kotlin.test.Test
import kotlin.test.assertEquals

class TestHttpUsersClient {
    @Test
    fun testGetInfo() {
        val expectedUser = User(id = 2, fullName = "Ivanov Ivan")
        val client = HttpUsersClient()
        assertEquals(expectedUser, runBlocking { client.getInfo(1) })
    }
}
