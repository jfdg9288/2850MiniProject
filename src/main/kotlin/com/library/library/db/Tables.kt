package com.library.library.db

import org.jetbrains.exposed.sql.Table

object Books : Table("books") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 512)
    val isbn = varchar("isbn", 32).nullable()
    override val primaryKey = PrimaryKey(id)
}

object Authors : Table("authors") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 256).uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}

object BookAuthors : Table("book_author") {
    val book_Id = integer("book_id").references(Books.id)
    val author_Id = integer("author_id").references(Authors.id)

    override val primaryKey = PrimaryKey(book_Id, author_Id)
}
