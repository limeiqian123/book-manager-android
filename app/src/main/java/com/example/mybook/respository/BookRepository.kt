package com.example.mybook.respository

import com.example.mybook.data.Book
import io.reactivex.Observable
import java.lang.Exception

class BookRepository {

    private var apiService: ApiService? = null

    init {
        apiService = RetrofitClient.getInstance()
    }

    fun getAllBooks() = apiService?.getAllBooks()

    fun addNewBook(book: Book) = apiService?.addBook(book)

    fun deleteBook(isbn: String) = apiService?.deleteBookByIsbn(isbn)

    fun updateBookByIsbn(isbn: String, book: Book) =
        apiService?.updateBookByIsbn(isbn, book)

}