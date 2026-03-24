import io.ktor.server.application.Application
import java.io.File
import java.io.InputStream
import org.jetbrains.kotlinx.dataframe.*
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.*



fun SortDataFrame(df: DataFrame<*>, name: String?): DataFrame<*> {
    // Very slow bubble sort
    // Deal with null types
    when (name) {
        null -> return df
    }
    val data = name
    val newDf = df.filter { "author".startsWith(data)}
    println(newDf)
    return df
}