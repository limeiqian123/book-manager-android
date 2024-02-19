package com.example.mybook.respository

import com.example.mybook.response.BookResponseData
import com.example.mybook.response.DeleteResponseData
import com.example.mybook.response.ResponseData
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("list")
    fun getAllBooks(): Observable<ResponseData>

    @GET("get/{id}")
    fun queryBookById(@Path("id") id: Int): Observable<BookResponseData>

    @POST("add")
    fun addBook(@Body requestBody: RequestBody): Observable<BookResponseData>

    @DELETE("delete/{id}")
    fun deleteBookById(@Path("id") id: Int): Observable<DeleteResponseData>

    @PUT("update")
    fun updateBook(@Body requestBody: RequestBody): Observable<BookResponseData>
}