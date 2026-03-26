package com.library.library.routes

import com.library.library.service.BookService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.registerBookRoutes() {
    val bookService = BookService()

    routing {
        get("/") {
            call.respondText("Library app running")
        }

        get("/books") {
            val sort = call.request.queryParameters["sort"]
            call.respond(bookService.listBooks(sort))
        }

        get("/books/search") {
            val query = call.request.queryParameters["q"]
            val sort = call.request.queryParameters["sort"]
            call.respond(bookService.searchBooks(query, sort))
        }
    }
}