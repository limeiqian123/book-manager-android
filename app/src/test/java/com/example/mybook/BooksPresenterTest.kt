package com.example.mybook

import com.example.mybook.data.Book
import com.example.mybook.response.BookResponseData
import com.example.mybook.response.Count
import com.example.mybook.response.DeleteResponseData
import com.example.mybook.response.ResponseData
import com.example.mybook.respository.BookRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner::class)
class BooksPresenterTest {

    private lateinit var presenter: BooksContract.Presenter

    @Mock
    private lateinit var view: BooksContract.View

    @MockK
    private lateinit var repository: BookRepository

    private val immediateScheduler = object : Scheduler() {
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
        }
    }


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setInitIoSchedulerHandler { immediateScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediateScheduler }
        repository = mockk()
        presenter = BooksPresenter()
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `test fetch book list`() {
        val response: Observable<ResponseData> =
            Observable.just(ResponseData(0, "", getMockBookList()))
        every { repository.getAllBooks() } returns response

        presenter.fetchBookListFromServer()
        verify(view).apply {
            showLoadingView()
            displayBookList(getMockBookList())
            hideLoadingView()
        }
    }

    @Test
    fun `test fetch book list fail`() {
        val response: Observable<ResponseData> =
            Observable.just(ResponseData(400, "", emptyList()))
        every { repository.getAllBooks() } returns response

        presenter.fetchBookListFromServer()
        verify(view).apply {
            showLoadingView()
            handleErrorView("Failed to retrieve book list.")
            hideLoadingView()
        }
    }

    @Test
    fun `test create a book`() {
        val newBook = getMockBookList()[0]
        val response: Observable<BookResponseData> =
            Observable.just(BookResponseData(0, "", newBook))
        every { repository.addNewBook(newBook) } returns response

        presenter.createBook(newBook)
        verify(view).apply {
            showSuccessMsg("Add a book successfully.")
            hideLoadingView()
        }
    }

    @Test
    fun `test get book by id`() {
        val book = getMockBookList()[0]
        val response: Observable<BookResponseData> =
            Observable.just(BookResponseData(0, "", book))
        every { repository.queryBookById(1) } returns response

        presenter.queryBookById(1)
        verify(view).apply {
            showLoadingView()
            showBookDetail(book)
            hideLoadingView()
        }
    }

    @Test
    fun `test update a book`() {
        val updatedBook = getMockBookList()[0]
        val response: Observable<BookResponseData> =
            Observable.just(BookResponseData(0, "", updatedBook))
        every { repository.updateBook(updatedBook) } returns response

        presenter.updateBook(updatedBook)
        verify(view).apply {
            showLoadingView()
            showSuccessMsg("Update book successfully.")
            hideLoadingView()
        }
    }

    @Test
    fun `test delete a book`() {
        val book = getMockBookList()[0]
        val response: Observable<DeleteResponseData> =
            Observable.just(DeleteResponseData(0, "", Count(1)))
        every { repository.deleteBook(1) } returns response

        presenter.deleteBook(book)
        verify(view).apply {
            showSuccessMsg("Delete successfully.")
            hideLoadingView()
        }
    }

    private fun getMockBookList() = listOf(
        Book(1,"9787011234560", "book1", "Mike", "1992"),
        Book(2, "9787011234890", "book2", "Joke", "1993"),
    )
}
