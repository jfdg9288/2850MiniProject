// Set up application routing and request handling

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        get("/") { call.displayForm() }
        get("/search") { call.handleBookSearch() }
    }
}

private suspend fun ApplicationCall.displayForm() {
    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Library" }
        content {
            h1 { +"Library" }
            form(action = "/search", method = FormMethod.get) {
                input {

                    type = InputType.text
                    id = "query"
                    name = "q"
                    placeholder = "search"
                }
                button { +"Search" }
            }

        }
    }
}


private suspend fun ApplicationCall.handleBookSearch() {
    val (name) = getDiceDetails(request)
    val results = diceRoll(num, die)

    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Search Results" }
        content {
            h1 { +"Library" }
            

            p { +"Showing results for" }

            form(action = "/search", method = FormMethod.get) {
                input {

                    type = InputType.text
                    id = "query"
                    name = "q"
                    placeholder = "search"
                }
            }

            p {
                +"The result was: "
                strong {
                    +"${results[0]}"
                    for (result in results.drop(1)) {
                        +", $result"
                    }
                }
            }

            p {
                +"For a total of "
                strong{ +"${results.sum()}" }
            }

            p {
                +"You can "
                a(request.uri) { +"repeat this roll" }
                +", or request a "
                a("/") { +"new roll" }
                +"."
            }
        }
    }
}

private fun getDiceDetails(request: ApplicationRequest) = Pair(
    request.queryParameters["num"]?.toInt() ?: error("Number of dice not specified"),
    request.queryParameters["die"] ?: error("Die not specified")
)
