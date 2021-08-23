package support

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.coroutineScope

typealias HeadersHandler = suspend PipelineContext<Unit, ApplicationCall>.() -> Unit


class CheckHeadersFeatures(config: Configuration) {
    private val handlers = config.handlers.toList()

    class Configuration {
        val handlers = mutableListOf<HeadersHandler>()

        fun checkHeaders(handler: HeadersHandler) {
            handlers.add(handler)
        }
    }

    private suspend fun interceptCall(context: PipelineContext<Unit, ApplicationCall>) {
        handlers.forEach { handler ->
            if (context.call.response.status() == null) {
                context.handler()
            }
            if (context.call.response.status() != null) {
                context.finish()
            }
        }
        coroutineScope {
            context.proceed()
        }
    }


    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, CheckHeadersFeatures> {
        override val key: AttributeKey<CheckHeadersFeatures> = AttributeKey("Check headers")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): CheckHeadersFeatures {
            val configuration = Configuration().apply(configure)
            val feature = CheckHeadersFeatures(configuration)

            if (feature.handlers.isNotEmpty()) {
                pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                    feature.interceptCall(this)
                }
            }
            return feature
        }
    }
}
