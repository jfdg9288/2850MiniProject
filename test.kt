import java.io.File
import java.io.InputStream
implementation("org.jetbrains.kotlinx:dataframe-csv:1.0.0-Beta4")

fun main () {
    val df = DataFrame.readCsv("library_booklist.csv")
    println(df)
}