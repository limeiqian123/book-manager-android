package com.example.mybook.respository

import com.example.mybook.data.Book
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    @GET("books")
    fun getAllBooks(): Observable<List<Book>>

    @POST("books")
    fun addBook(@Body book: Book): Observable<String>

    @PUT("books/{isbn}")
    fun updateBookByIsbn(@Path("isbn") isbn: String, @Body book: Book): Observable<String>

    @DELETE("books/{isbn}")
    fun deleteBookByIsbn(@Path("isbn") isbn: String): Observable<String>
}