package com.library.library.db

import com.library.library.model.AuthorDto
import com.library.library.model.BookDto
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BookRepository {

    fun isBooksEmpty(): Boolean = transaction {
        Books.selectAll().empty()
    }

    fun getAllBooks(sort: String): List<BookDto> = transaction {
        val rows = baseBookQuery()
            .applySort(sort)
            .toList()

        mapRowsToBooks(rows)
    }

    fun searchBooks(query: String, sort: String): List<BookDto> = transaction {
        val normalized = query.trim().lowercase()
        val containsTerm = "%$normalized%"

        val rows = baseBookQuery()
            .searchFilter(containsTerm)
            .applySort(sort)
            .toList()

        rankBooks(mapRowsToBooks(rows), normalized)
    }

    fun findOrCreateAuthor(name: String): Int = transaction {
        val existing = Authors
            .selectAll()
            .where { Authors.name eq name }
            .singleOrNull()

        if (existing != null) return@transaction existing[Authors.id]

        Authors.insert {
            it[Authors.name] = name
        }[Authors.id]
    }

    fun findOrCreateBook(title: String, isbn: String?): Int = transaction {
        val existing = if (!isbn.isNullOrBlank()) {
            Books.selectAll().where { Books.isbn eq isbn }.singleOrNull()
        } else {
            Books.selectAll().where { Books.title eq title }.singleOrNull()
        }

        if (existing != null) return@transaction existing[Books.id]

        Books.insert {
            it[Books.title] = title
            it[Books.isbn] = isbn
        }[Books.id]
    }

    fun linkBookAuthor(bookId: Int, authorId: Int) = transaction {
        val exists = BookAuthors.selectAll().where {
            (BookAuthors.book_Id eq bookId) and (BookAuthors.author_Id eq authorId)
        }.any()

        if (!exists) {
            BookAuthors.insert {
                it[book_Id] = bookId
                it[author_Id] = authorId
            }
        }
    }

    private fun baseBookQuery(): Query {
        return Books
            .leftJoin(BookAuthors, { Books.id }, { BookAuthors.book_Id })
            .leftJoin(Authors, { BookAuthors.author_Id }, { Authors.id })
            .selectAll()
    }

    private fun Query.searchFilter(containsTerm: String): Query {
        return andWhere {
            (Books.title.lowerCase() like containsTerm) or
                    (Authors.name.lowerCase() like containsTerm)
        }
    }

    private fun Query.applySort(sort: String): Query {
        return when (sort.lowercase()) {
            "author" -> orderBy(Authors.name to SortOrder.ASC, Books.title to SortOrder.ASC)
            "title" -> orderBy(Books.title to SortOrder.ASC)
            else -> orderBy(Books.title to SortOrder.ASC)
        }
    }

    private fun mapRowsToBooks(rows: List<org.jetbrains.exposed.sql.ResultRow>): List<BookDto> {
        return rows
            .groupBy { it[Books.id] }
            .map { (bookId, bookRows) ->
                val first = bookRows.first()

                val authors = bookRows
                    .mapNotNull { row ->
                        row.getOrNull(Authors.id)?.let { authorId ->
                            val authorName = row.getOrNull(Authors.name) ?: return@let null
                            AuthorDto(authorId, authorName)
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

    private fun rankBooks(books: List<BookDto>, query: String): List<BookDto> {
        return books.sortedWith(
            compareBy<BookDto>(
                { if (it.title.equals(query, ignoreCase = true)) 0 else 1 },
                { if (it.authors.any { author -> author.name.equals(query, ignoreCase = true) }) 0 else 1 },
                { if (it.title.lowercase().startsWith(query)) 0 else 1 },
                { if (it.authors.any { author -> author.name.lowercase().startsWith(query) }) 0 else 1 },
                { if (it.title.lowercase().contains(query)) 0 else 1 },
                { it.title.lowercase() }
            )
        )
    }
}