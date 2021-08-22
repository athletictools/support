package support.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import support.repositories.PostgresMessageRepository
import support.services.ClientSupportService


val di = DI {
    bind<ChatRepository>() with singleton { PostgresMessageRepository() }
    bind<SupportService>() with singleton { ClientSupportService(instance()) }
}
