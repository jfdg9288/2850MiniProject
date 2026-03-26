package com.library.library.load

import com.library.library.db.BookRepository
import com.opencsv.CSVReader
import java.io.InputStreamReader

object CsvImporter {

    private val repository = BookRepository()

    fun importIfEmpty() {
        if (!repository.isBooksEmpty()) return

        val stream = this::class.java.getResourceAsStream("/library_booklist.csv")
            ?: error("library_booklist.csv not found in src/main/resources")

        CSVReader(InputStreamReader(stream)).use { csv ->
            val header = csv.readNext() ?: return

            val idxTitle = header.indexOfFirst { it.equals("title", ignoreCase = true) }
            val idxAuthor = header.indexOfFirst { it.equals("author", ignoreCase = true) }
            val idxIsbn = header.indexOfFirst { it.equals("isbn_13", ignoreCase = true) }

            if (idxTitle == -1 || idxAuthor == -1) {
                error("CSV must include columns: title, author (and optionally isbn_13).")
            }

            var count = 0
            while (true) {
                val row = csv.readNext() ?: break
                val title = row.getOrNull(idxTitle)?.trim().orEmpty()
                val authorName = row.getOrNull(idxAuthor)?.trim().orEmpty()
                val isbn = row.getOrNull(idxIsbn)?.trim()?.takeIf { it.isNotBlank() }

                if (title.isBlank() || authorName.isBlank()) continue

                val authorId = repository.findOrCreateAuthor(authorName)
                val bookId = repository.findOrCreateBook(title, isbn)
                repository.linkBookAuthor(bookId, authorId)

                count++
            }

            println("CSV import finished. Rows processed: $count")
        }
    }
}