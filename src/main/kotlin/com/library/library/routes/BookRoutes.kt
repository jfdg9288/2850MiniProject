package com.library.library.routes

import com.library.library.db.Queries
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerBookRoutes() {
    routing {
        get("/") {
            call.respondText("Library app running")
        }

        get("/books") {
            val sort = call.request.queryParameters["sort"]
            val result = Queries.getBooks(sort)   // MUST return List<BookDto>
            call.respond(result)
        }
    }
}
