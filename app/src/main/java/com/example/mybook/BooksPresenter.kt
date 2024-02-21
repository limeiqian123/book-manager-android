package com.example.mybook

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.text.Editable
import android.view.View
import android.widget.EditText
import com.example.mybook.adapter.BookAdapter
import com.example.mybook.adapter.ItemClickListener
import com.example.mybook.base.BasePresenter
import com.example.mybook.data.Book
import com.example.mybook.respository.BookRepository

class BooksPresenter: BasePresenter<BooksContract.View>(), BooksContract.Presenter, ItemClickListener {

    private val repository: BookRepository = BookRepository()
    private lateinit var adapter: BookAdapter

    override fun setAdapter(adapter: BookAdapter) {
        this.adapter = adapter
        adapter.setOnItemClickListener(this)
    }
    override fun fetchBookListFromServer() {
        repository.getAllBooks()?.doAfterTerminate { mView?.hideLoadingView()}
            ?.doOnSubscribe {
               mView?.showLoadingView()
            }
            ?.subscribe ({ response ->
                if (response.code == 0 && response.data.isNotEmpty()) {
                    mView?.displayBookList(response.data)
                } else {
                    mView?.handleErrorView("Failed to retrieve book list.")
                }
            }, { error ->
                mView?.handleErrorView("An error occurred while retrieving book list.\\n${error.message}\"")
            })
    }

    override fun createBook(book: Book) {
        repository.addNewBook(book)
            ?.doOnSubscribe {
                mView?.showLoadingView()
            }
            ?.doAfterTerminate { mView?.hideLoadingView() }
            ?.subscribe({ response ->
                if (response.code == 0) {
                    mView?.showSuccessMsg("Add a book successfully.")
                    refreshData()
                } else {
                    mView?.handleErrorView("Fail to add a book.")
                }
            }, { error ->
                mView?.handleErrorView("An error occurred while adding a book.\\n${error.message}\"")

            })
    }

    @SuppressLint("CheckResult")
    override fun queryBookById(id: Int) {
        repository.queryBookById(id)
            ?.doOnSubscribe { mView?.showLoadingView() }
            ?.doAfterTerminate { mView?.hideLoadingView() }
            ?.subscribe({ response ->
                if (response.code == 0 && response.data != null) {
                    mView?.showBookDetail(response.data)
                } else {
                    mView?.handleErrorView("Fail to query a book.")
                }
            }, {error ->
                mView?.handleErrorView("An error occurred while querying a book.\\n${error.message}\"")
            })
    }

    override fun onItemClick(book: Book) {
        book.id?.let {
            queryBookById(it)
        }
    }

    override fun updateBook(book: Book) {
        repository.updateBook(book)
            ?.doOnSubscribe { mView?.showLoadingView() }
            ?.doAfterTerminate { mView?.hideLoadingView() }
            ?.subscribe({response ->
                if (response.code == 0 && response.data != null) {
                    mView?.showSuccessMsg("Update book successfully.")
                    refreshData()
                } else {
                    mView?.handleErrorView("Fail to update a book.")
                }
            }, { error ->
                mView?.handleErrorView("An error occurred while updating a book.\\n${error.message}\"")
            })
    }

    override fun deleteBook(book: Book) {
        book.id?.let {
            repository.deleteBook(it)
                ?.doOnSubscribe { mView?.showLoadingView() }
                ?.doAfterTerminate { mView?.hideLoadingView() }
                ?.subscribe({ response ->
                    if (response.code == 0) {
                        mView?.showSuccessMsg("Delete successfully.")
                        refreshData()
                    } else {
                        mView?.handleErrorView("Fail to delete a book.")
                    }
                }, { error ->
                    mView?.handleErrorView("An error occurred while deleting a book.\\n${error.message}\"")
                })
        }
    }

    override fun onUpdateButtonClick(book: Book) {
        mView?.showBookUpdateDialog(book)
    }

    override fun onDeleteButtonClick(book: Book) {
        mView?.showBookDeleteDialog(book)
    }

    override fun refreshData() {
        fetchBookListFromServer()
    }

    override fun bindBookDialogData(bookView: View, book: Book, editable: Boolean) {
        val dateEditText = bookView.findViewById<EditText>(R.id.bookDateEditText)
        val nameEditText = bookView.findViewById<EditText>(R.id.bookNameEditText)
        val isbnEditText = bookView.findViewById<EditText>(R.id.bookIsbnEditText)
        val authorEditText = bookView.findViewById<EditText>(R.id.bookAuthorEditText)
        dateEditText.apply {
            setText(book.publishTime)
            //just need date picker focus
            isFocusableInTouchMode = false
            if (editable)
                dateEditText.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
                        dateEditText.text = Editable.Factory.getInstance()
                            .newEditable("$year-${monthOfYear + 1}-$dayOfMonth")
                    }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
                        .show()
                }
        }
        nameEditText.apply {
            text = Editable.Factory.getInstance().newEditable(book.bookName)
            isFocusableInTouchMode = editable
        }
        isbnEditText.apply {
            text = Editable.Factory.getInstance().newEditable(book.isbn)
            isFocusableInTouchMode = editable
        }
        authorEditText.apply {
            text = Editable.Factory.getInstance().newEditable(book.author)
            isFocusableInTouchMode = editable
        }
    }

}
