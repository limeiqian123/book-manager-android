package com.example.mybook

import com.example.mybook.data.Book
import com.example.mybook.respository.ApiService
import com.example.mybook.respository.BookRepository
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
        bookRepository = BookRepository()
    }

    @Test
    fun `test get all books successfully`() {
        val books = getMockBooks()
        `when`(mockApiService.getAllBooks()).thenReturn(Observable.just(books))
        assertEquals(bookRepository.getAllBooks(), books)
    }

    @Test
    fun `test add new book successfully`() {
        val newBook = Book("9787011324560", "book", "Mike", "2011-02-01")
        `when`(mockApiService.addBook(newBook)).thenReturn(Observable.just("Success"))
        assertEquals(bookRepository.addNewBook(newBook), "Success")
    }

    @Test
    fun `test add new book fail with network issue`() {

    }

    @Test
    fun `test add new book fail with wrong isbn`() {

    }

    @Test
    fun `test delete book by isbn successfully`() {
        `when`(mockApiService.deleteBookByIsbn(anyString()))
            .thenReturn(Observable.just("Success"))
        assertEquals(bookRepository.deleteBook("9787011234890"), "Success")
    }

    @Test
    fun `test delete book fail with network issue`() {

    }

    @Test
    fun `test update book by isbn successfully`() {
        val updateBook = Book("9787011234560", "updateBook", "Mike", "2020-02-01")
        `when`(mockApiService.updateBookByIsbn(anyString(), anyObject<Book>()))
            .thenReturn(Observable.just("Success"))
        assertEquals(bookRepository.updateBookByIsbn("9787011234560", updateBook), "Success")
    }

    @Test
    fun `test update book fail with network issue`() {

    }

    private fun getMockBooks() = listOf<Book>(
        Book("9787011234560", "book1", "Mike", "2020-02-01"),
        Book("9787011234890", "book2", "Joke", "2020-09-01"),
    )
}