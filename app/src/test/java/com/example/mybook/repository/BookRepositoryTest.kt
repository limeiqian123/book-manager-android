package com.example.mybook.repository

import com.example.mybook.data.Book
import com.example.mybook.response.ResponseData
import com.example.mybook.respository.ApiService
import com.example.mybook.respository.BookRepository
import com.example.mybook.respository.RetrofitClient
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyObject
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BookRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var bookRepository: BookRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockApiService = RetrofitClient.getInstance()
        bookRepository = BookRepository()
    }

    @Test
    fun `test get all books successfully`() {
        `when`(mockApiService.getAllBooks()).thenReturn(getMockResponseData())
        assertEquals(bookRepository.getAllBooks(), getMockResponseData())
    }

    @Test
    fun `test add new book successfully`() {

    }

    @Test
    fun `test add new book fail with network issue`() {

    }

    @Test
    fun `test add new book fail with wrong isbn`() {

    }

    @Test
    fun `test delete book by isbn successfully`() {

    }

    @Test
    fun `test delete book fail with network issue`() {

    }

    @Test
    fun `test update book by isbn successfully`() {

    }

    @Test
    fun `test update book fail with network issue`() {

    }

    private fun getMockBooks() = listOf(
        Book(1,"9787011234560", "book1", "Mike", "1992"),
        Book(2, "9787011234890", "book2", "Joke", "1993"),
    )

    private fun getMockResponseData(): Observable<ResponseData> = Observable.just(ResponseData(0, "", getMockBooks()))
}