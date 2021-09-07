package support.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import support.clients.HttpUsersClient
import support.repositories.MongoChatRepository
import support.services.ClientSupportService
import java.util.*


val di = DI {
    bind<Now>() with singleton { Now {Date()} }
    bind<CoroutineDatabase>() with singleton { KMongo.createClient().getDatabase("support").coroutine }
    bind<ChatRepository>() with singleton { MongoChatRepository(instance()) }
    bind<SupportService>() with singleton { ClientSupportService(instance(), instance(), instance()) }
    bind<UsersClient>() with singleton { HttpUsersClient() }
}
