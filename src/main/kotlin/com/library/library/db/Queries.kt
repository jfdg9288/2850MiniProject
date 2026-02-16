package com.library.library.db

import com.library.library.model.AuthorDto
import com.library.library.model.BookDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Queries {

    fun isBooksEmpty(): Boolean = transaction {
        Books.selectAll().empty()
    }

    fun getBooks(sort: String?): List<BookDto> = transaction {
        val base_Query = Books
            .leftJoin(BookAuthors, { Books.id }, { BookAuthors.book_Id })
            .leftJoin(Authors, { BookAuthors.author_Id }, { Authors.id })
            .slice(Books.id, Books.title, Authors.id, Authors.name)
            .selectAll()

        val ordered_Query = when (sort) {
            "title" -> base_Query.orderBy(Books.title to SortOrder.ASC)
            "author" -> base_Query.orderBy(Authors.name to SortOrder.ASC, Books.title to SortOrder.ASC)
            else -> base_Query.orderBy(Books.title to SortOrder.ASC)
        }

        // group rows into books with author lists
        val rows = ordered_Query.toList()
        val grouped = rows.groupBy { it[Books.id] }

        grouped.map { (book_Id, book_Rows) ->
            val title = book_Rows.first()[Books.title]
            val authors = book_Rows
                .mapNotNull { r ->
                    val a_id = r[Authors.id]
                    val a_name = r[Authors.name]
                    if (a_id == null || a_name == null) null else AuthorDto(a_id, a_name)
                }
                .distinctBy { it.id }

            BookDto(book_Id, title, authors)
        }
    }

    fun findOrCreateAuthor(name: String): Int = transaction {
        val existing = Authors.select { Authors.name eq name }.singleOrNull()
        if (existing != null) return@transaction existing[Authors.id]

        Authors.insert {
            it[Authors.name] = name
        } get Authors.id
    }

    fun findOrCreateBook(title: String, isbn: String?): Int = transaction {
        val existing = if (!isbn.isNullOrBlank()) {
            Books.select { Books.isbn eq isbn }.singleOrNull()
        } else {
            Books.select { Books.title eq title }.singleOrNull()
        }

        if (existing != null) return@transaction existing[Books.id]

        Books.insert {
            it[Books.title] = title
            it[Books.isbn] = isbn
        } get Books.id
    }

    fun linkBookAuthor(book_Id: Int, author_Id: Int) = transaction {
        val exists = BookAuthors.select {
            (BookAuthors.book_Id eq book_Id) and (BookAuthors.author_Id eq author_Id)
        }.any()

        if (!exists) {
            BookAuthors.insert {
                it[BookAuthors.book_Id] = book_Id
                it[BookAuthors.author_Id] = author_Id
            }
        }
    }
}
