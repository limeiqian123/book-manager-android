package com.example.mybook.respository

import com.example.mybook.data.Book
import com.example.mybook.response.BookResponseData
import com.example.mybook.response.ResponseData
import com.example.mybook.response.ResponseObjectData
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody

class BookRepository {

    private var apiService: ApiService? = null

    //private val gson = GsonBuilder().create()

    init {
        apiService = RetrofitClient.getInstance()
    }

    fun getAllBooks(): Observable<ResponseData>? {
        return apiService?.getAllBooks()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun addNewBook(book: Book): Observable<BookResponseData>? {
        val requestBody = RequestBody.create(MediaType.parse("application/json"), convertToJsonParams(book))
        return apiService?.addBook(requestBody)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    private fun convertToJsonParams(book: Book): String {
        return GsonBuilder().create().toJson(book)
    }

    fun deleteBook(id: Int): Observable<ResponseObjectData>? {
       return apiService?.deleteBookById(id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun queryBookById(id: Int): Observable<BookResponseData>? {
        return apiService?.queryBookById(id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun updateBook(book: Book): Observable<BookResponseData>? {
        val requestBody = RequestBody.create(MediaType.parse("application/json"), convertToJsonParams(book))
        return apiService?.updateBook(requestBody)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
    }

}