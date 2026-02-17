// Custom pages for 404 and 500 errors

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.plugins.statuspages.StatusPages
import kotlinx.html.*

fun Application.configureErrorHandling() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondHtmlTemplate(LayoutTemplate(), status = status) {
                titleText { +"Error: Die Roller" }
                content {
                    h1 { +"404 Page Not Found" }
                    p { +"Oh no!" }
                    p { +"Looks like you've used an invalid URL..." }
                }
            }
        }

        exception<Throwable> { call, error ->
            call.respondHtmlTemplate(LayoutTemplate(), HttpStatusCode.InternalServerError) {
                titleText { +"Error: Die Roller" }
                content {
                    h1 { +"500 Internal Server Error" }
                    p { +"${error.message}" }
                }
            }
        }
    }
}
