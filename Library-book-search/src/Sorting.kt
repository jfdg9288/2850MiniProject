import io.ktor.server.application.Application
import java.io.File
import java.io.InputStream
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.*
import kotlin.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths



fun SortDataFrame(df: DataFrame<*>, name: String?): List<String> {
    // Very slow bubble sort
    // Deal with null types
    when (name) {
        null -> return mutableListOf<String>()
    }

    var list = mutableListOf<String>()
    // Make sure every element of the dataframe is a string
    for (i in 0 .. df.rowsCount()-1) {
        var item = df["title"][i]
        var result = when {
            item is String -> item
            else -> throw IllegalArgumentException()
        }
        list.add(result)
    }

    val newList = list.filter { it.startsWith(name)}
    return newList
}