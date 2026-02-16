package com.library.library.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        // H2 database that resets each run.
        Database.connect(
            url = "jdbc:h2:mem:library;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        transaction {
            SchemaUtils.create(Books, Authors, BookAuthors)
        }
    }
}
