package com.library.library.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val id: Int,
    val name: String
)

@Serializable
data class BookDto(
    val id: Int,
    val title: String,
    val authors: List<AuthorDto>,
    val isbn: String? = null
)

@Serializable
data class BookSearchResponse(
    val items: List<BookDto>,
    val total: Int,
    val query: String? = null,
    val sort: String = "title"
)