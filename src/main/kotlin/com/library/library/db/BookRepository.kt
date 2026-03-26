package com.library.library.db

import com.library.library.model.AuthorDto
import com.library.library.model.BookDto
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BookRepository {

    fun isBooksEmpty(): Boolean = transaction {
        Books.selectAll().empty()
    }

    fun getAllBooks(sort: String): List<BookDto> = transaction {
        val baseQuery = Books
            .leftJoin(BookAuthors)
            .leftJoin(Authors)
            .slice(Books.id, Books.title, Books.isbn, Authors.id, Authors.name)
            .selectAll()

        val orderedQuery = when (sort.lowercase()) {
            "author" -> baseQuery.orderBy(Authors.name to SortOrder.ASC, Books.title to SortOrder.ASC)
            else -> baseQuery.orderBy(Books.title to SortOrder.ASC)
        }

        mapRowsToBooks(orderedQuery.toList())
    }

    fun searchBooks(query: String, sort: String): List<BookDto> = transaction {
        val q = query.trim().lowercase()
        val containsTerm = "%$q%"

        val baseQuery = Books
            .leftJoin(BookAuthors)
            .leftJoin(Authors)
            .slice(Books.id, Books.title, Books.isbn, Authors.id, Authors.name)
            .select {
                Op.build {
                    (Books.title.lowerCase() like containsTerm) or
                            (Authors.name.lowerCase() like containsTerm)
                }
            }

        val orderedQuery = when (sort.lowercase()) {
            "author" -> baseQuery.orderBy(Authors.name to SortOrder.ASC, Books.title to SortOrder.ASC)
            else -> baseQuery.orderBy(Books.title to SortOrder.ASC)
        }

        val books = mapRowsToBooks(orderedQuery.toList())

        books.sortedWith(
            compareBy<BookDto>(
                { if (it.title.equals(query, ignoreCase = true)) 0 else 1 },
                { if (it.authors.any { author -> author.name.equals(query, ignoreCase = true) }) 0 else 1 },
                { if (it.title.lowercase().startsWith(q)) 0 else 1 },
                { if (it.authors.any { author -> author.name.lowercase().startsWith(q) }) 0 else 1 },
                { if (it.title.lowercase().contains(q)) 0 else 1 },
                { it.title.lowercase() }
            )
        )
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

    fun linkBookAuthor(bookId: Int, authorId: Int) = transaction {
        val exists = BookAuthors.select {
            (BookAuthors.book_Id eq bookId) and (BookAuthors.author_Id eq authorId)
        }.any()

        if (!exists) {
            BookAuthors.insert {
                it[BookAuthors.book_Id] = bookId
                it[BookAuthors.author_Id] = authorId
            }
        }
    }

    private fun mapRowsToBooks(rows: List<ResultRow>): List<BookDto> {
        return rows
            .groupBy { it[Books.id] }
            .map { (bookId, bookRows) ->
                val first = bookRows.first()

                val authors = bookRows
                    .mapNotNull { row ->
                        val authorId = row.getOrNull(Authors.id)
                        val authorName = row.getOrNull(Authors.name)

                        if (authorId != null && authorName != null) {
                            AuthorDto(authorId, authorName)
                        } else {
                            null
                        }
                    }
                    .distinctBy { it.id }

                BookDto(
                    id = bookId,
                    title = first[Books.title],
                    authors = authors,
                    isbn = first[Books.isbn]
                )
            }
    }
}