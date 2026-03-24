import java.io.File
import java.io.InputStream

fun main () {
  val inputStream: InputStream = File ("library_booklist.csv").inputStream()
  val inputString = inputStream.reader().use {it.readText()}
  println (inputString)
}