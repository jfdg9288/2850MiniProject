package com.library.library

import com.library.library.db.DatabaseFactory
import com.library.library.load.CsvImporter
import com.library.library.routes.registerBookRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }

    // connects DB and creates tables
    DatabaseFactory.init()

    // loads CSV into DB if DB empty
    CsvImporter.importIfEmpty()

    // routes
    registerBookRoutes()
}
