// Application entry point

import io.ktor.server.application.Application
import java.io.File
import java.io.InputStream
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.*

fun Application.module() {
    configureErrorHandling()
    configureRouting()
}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

// code from https://kotlin.github.io/dataframe/read.html#read-from-csv
fun getBooks(): DataFrame<*> {
    val bookList = DataFrame.readCsv(File("resources/library_booklist.csv")).select { "title" and "author" }.distinct()
    return bookList
}