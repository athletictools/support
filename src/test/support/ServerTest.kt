package support

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals


class ServerTest {
    @Test
    fun testMessages() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/public/messages").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello, world!", response.content)
            }
        }
    }

    @Test
    fun testCreateMessage() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/public/create-message").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("", response.content)
            }
        }
    }
}
