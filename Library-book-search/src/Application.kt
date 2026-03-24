// Application entry point

import io.ktor.server.application.Application
import java.io.File
import java.io.InputStream

fun Application.module() {
    configureErrorHandling()
    configureRouting()
}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

// Code for getting file is taken from https://www.geeksforgeeks.org/kotlin/read-from-files-using-inputreader-in-kotlin/
fun getBooks() {
    val inputStream: InputStream = File ("resources/library_booklist.csv").inputStream()
    val inputString = inputStream.reader().use {it.readText()}
    println (inputString)
}