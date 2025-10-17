package com.example.devoir_3


class BookService {
    fun getBooks(): List<Book> {
        return listOf(
            Book("Le Petit Prince", "Antoine de Saint-Exupéry"),
            Book("Les Misérables", "Victor Hugo"),
            Book("L’Étranger", "Albert Camus"),
            Book("Harry Potter", "J.K. Rowling")
        )
    }
}