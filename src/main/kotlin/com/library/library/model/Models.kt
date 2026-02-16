package com.library.library.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(val id: Int, val name: String)

@Serializable
data class BookDto(val id: Int, val title: String, val authors: List<AuthorDto>)
