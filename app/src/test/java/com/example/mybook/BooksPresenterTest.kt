package com.example.mybook

import com.example.mybook.data.Book
import com.example.mybook.response.ResponseData
import com.example.mybook.respository.BookRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BooksPresenterTest {

    private lateinit var presenter: BooksContract.Presenter

    @Mock
    private lateinit var view: BooksContract.View

    private lateinit var repository: BookRepository


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = BookRepository()
        presenter = BooksPresenter()
        presenter.attachView(view)
    }
    @Test
    fun `test fetch book list`() {
        `when`(repository.getAllBooks()).thenReturn(getMockResponseData())
        presenter.fetchBookListFromServer()
        verify(view).apply {
            showLoadingView()
            displayBookList(getMockBookList())
            hideLoadingView()
        }

    }

    @Test
    fun `bind book data to dialog`() {

    }

    private fun getMockBookList() = listOf(
        Book(1,"9787011234560", "book1", "Mike", "1992"),
        Book(2, "9787011234890", "book2", "Joke", "1993"),
    )

    private fun getMockResponseData(): Observable<ResponseData> = Observable.just(ResponseData(0, "", getMockBookList()))

}