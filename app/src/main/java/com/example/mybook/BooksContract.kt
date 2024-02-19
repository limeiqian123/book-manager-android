package com.example.mybook

import com.example.mybook.adapter.BookAdapter
import com.example.mybook.base.BaseContract
import com.example.mybook.data.Book

interface BooksContract {

    interface View: BaseContract.BaseView {
        fun showLoadingView()
        fun hideLoadingView()
        fun displayBookList(bookList: List<Book>)
        fun handleErrorView(errorMsg: String)
        fun showBookDetail(book: Book)
        fun showBookUpdateDialog(book: Book)
        fun showBookDeleteDialog(book: Book)
        fun showSuccessMsg(msg: String)
    }

    interface Presenter: BaseContract.BasePresenter<View> {
        fun fetchBookListFromServer()
        fun createBook(book: Book)
        fun queryBookById(id: Int)
        fun updateBook(book: Book)
        fun deleteBook(book: Book)

        fun refreshData()
        fun setAdapter(adapter: BookAdapter)
        fun bindBookDialogData(bookView: android.view.View, book: Book, editable: Boolean)
    }
}