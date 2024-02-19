package com.example.mybook.repository

import com.example.mybook.data.Book
import com.example.mybook.response.BookResponseData
import com.example.mybook.response.Count
import com.example.mybook.response.DeleteResponseData
import com.example.mybook.response.ResponseData
import com.example.mybook.respository.ApiService
import com.example.mybook.respository.BookRepository
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner::class)
class BookRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var bookRepository: BookRepository

    private val immediateScheduler = object : Scheduler() {
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
        }
    }


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
        bookRepository = BookRepository()
        bookRepository.setApiService(mockApiService)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `test get all books successfully`() {
        val responseData = ResponseData(0, "", getMockBooks())
        `when`(mockApiService.getAllBooks()).thenReturn(Observable.just(responseData))

        val result = bookRepository.getAllBooks()?.blockingFirst()
        verify(mockApiService).getAllBooks()
        assertEquals(2, result?.data?.size)
    }

    @Test
    fun `test add new book successfully`() {
        val newBook = getMockBooks()[0]
        val responseData = BookResponseData(0, "", newBook)
        `when`(mockApiService.addBook(any())).thenReturn(Observable.just(responseData))

        val result = bookRepository.addNewBook(newBook)?.blockingFirst()
        verify(mockApiService).addBook(any())
        assertEquals("book1", result?.data?.bookName)
    }

    @Test
    fun `test delete book by id successfully`() {
        val responseData = DeleteResponseData(0, "", Count(1))
        `when`(mockApiService.deleteBookById(1)).thenReturn(Observable.just(responseData))

        val result = bookRepository.deleteBook(1)?.blockingFirst()
        verify(mockApiService).deleteBookById(1)
        assertEquals(1, result?.data?.count)
    }
    @Test
    fun `test query book by id`() {
        val book = getMockBooks()[0]
        val responseData = BookResponseData(0, "", book)
        `when`(mockApiService.queryBookById(1)).thenReturn(Observable.just(responseData))

        val result = bookRepository.queryBookById(1)?.blockingFirst()
        verify(mockApiService).queryBookById(1)
        assertEquals("book1", result?.data?.bookName)
    }

    @Test
    fun `test update a book`() {
        val book = getMockBooks()[0]
        val responseData = BookResponseData(0, "", book)
        `when`(mockApiService.updateBook(any())).thenReturn(Observable.just(responseData))

        val result = bookRepository.updateBook(book)?.blockingFirst()
        verify(mockApiService).updateBook(any())
        assertEquals("book1", result?.data?.bookName)
    }

    private fun getMockBooks() = listOf(
        Book(1,"9787011234560", "book1", "Mike", "1992"),
        Book(2, "9787011234890", "book2", "Joke", "1993"),
    )

    private fun convertToJsonParams(book: Book): String {
        return GsonBuilder().create().toJson(book)
    }

}
