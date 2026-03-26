package com.library.library.service

import com.library.library.db.BookRepository
import com.library.library.model.BookSearchResponse

class BookService(
    private val repository: BookRepository = BookRepository()
) {
    fun listBooks(sort: String?): BookSearchResponse {
        val safeSort = normalizeSort(sort)
        val items = repository.getAllBooks(safeSort)
        return BookSearchResponse(
            items = items,
            total = items.size,
            sort = safeSort
        )
    }

    fun searchBooks(query: String?, sort: String?): BookSearchResponse {
        val trimmed = query?.trim().orEmpty()
        val safeSort = normalizeSort(sort)

        if (trimmed.isBlank()) {
            return BookSearchResponse(
                items = emptyList(),
                total = 0,
                query = trimmed,
                sort = safeSort
            )
        }

        val items = repository.searchBooks(trimmed, safeSort)

        return BookSearchResponse(
            items = items,
            total = items.size,
            query = trimmed,
            sort = safeSort
        )
    }

    private fun normalizeSort(sort: String?): String {
        return when (sort?.lowercase()) {
            "author" -> "author"
            else -> "title"
        }
    }
}