package com.library.library.routes

import com.library.library.db.Queries
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import com.library.library.model.BookDto

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
        
        get("/books/search") {
            // Add book searching endpoint - Henry
            // Query endpoint search with parameter q (query)
            // Returns books ordered by how well they match

            val query = call.request.queryParameters["q"]

            if (query.isNullOrBlank()) {
                // If empty query return nothing
                call.respond(emptyList<BookDto>())
                return@get
            }

            // Successful query return book list
            val result: List<BookDto> = Queries.searchBooks(query)
            call.respond(result)
        }
    }
}
