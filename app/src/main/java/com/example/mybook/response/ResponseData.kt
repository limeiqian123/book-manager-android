package com.example.mybook.response

import com.example.mybook.data.Book
import java.io.Serializable
import java.util.Objects


data class ResponseData(
    val code: Int,
    val message: String,
    val data: List<Book> = ArrayList()
) : Serializable

data class DeleteResponseData(
    val code: Int,
    val message: String,
    val data: Count
)

data class Count(val count: Int)

data class BookResponseData(
    val code: Int,
    val message: String,
    val data: Book?
)
