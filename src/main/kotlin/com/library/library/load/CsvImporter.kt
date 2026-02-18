package com.library.library.load

import com.library.library.db.Queries
import com.opencsv.CSVReader
import java.io.InputStreamReader

object CsvImporter {

    fun importIfEmpty() {
        if (!Queries.isBooksEmpty()) return

        val stream = this::class.java.getResourceAsStream("/library_booklist.csv")
            ?: error("library_booklist.csv not found in src/main/resources")

        CSVReader(InputStreamReader(stream)).use { csv ->
            val header = csv.readNext() ?: return

            // find column indexes by header name
            val idx_Title = header.indexOfFirst { it.equals("title", ignoreCase = true) }
            val idx_Author = header.indexOfFirst { it.equals("author", ignoreCase = true) }
            val idx_Isbn = header.indexOfFirst { it.equals("isbn_13", ignoreCase = true) }

            if (idx_Title == -1 || idx_Author == -1) {
                error("CSV must include columns: title, author (and optionally isbn_13).")
            }

            var count = 0
            while (true) {
                val row = csv.readNext() ?: break
                val title = row.getOrNull(idx_Title)?.trim().orEmpty()
                val author_Name = row.getOrNull(idx_Author)?.trim().orEmpty()
                val isbn = if (idx_Isbn >= 0) row.getOrNull(idx_Isbn)?.trim() else null

                if (title.isBlank() || author_Name.isBlank()) continue

                val author_Id = Queries.findOrCreateAuthor(author_Name)
                val book_Id = Queries.findOrCreateBook(title, isbn)
                Queries.linkBookAuthor(book_Id, author_Id)

                count++
            }

            println("CSV import finished. Rows processed: $count")
        }
    }
}
