// Set up application routing and request handling

import io.ktor.server.application.Application
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.*
import java.io.File
import java.io.InputStream

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
                    name = "name"
                    placeholder = "search"
                    required = true
                }
                button { +"Search" }
            }
        }
    }
}


private suspend fun ApplicationCall.handleBookSearch() {
    var name = getBookDetails(request)
    val books = getBooks()
    val titles = books["title"]
    println(titles)
    

    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Search Results" }
        content {
            form(action = "/", method = FormMethod.get) {
                button { +"Home" }
            }
            h1 { +"Library" }
            p { +"Showing results for ${name}" }
            //{% for result in books %}
        }
    }
}



private fun getBookDetails(request: ApplicationRequest) = (
    request.queryParameters["name"]
)