package com.example.mybook

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybook.adapter.BookAdapter
import com.example.mybook.base.BaseFragment
import com.example.mybook.data.Book
import com.example.mybook.databinding.FragmentBooksBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BooksFragment : BaseFragment<BooksContract.View, BooksContract.Presenter>(), BooksContract.View {

    private var _binding: FragmentBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: BookAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        adapter = BookAdapter()
        binding.bookRecycleView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        mPresenter?.setAdapter(adapter)
        mPresenter?.fetchBookListFromServer()
        return binding.root
    }

    override fun displayBookList(bookList: List<Book>) {
        adapter.setData(bookList)
    }

    override fun handleErrorView(errorMsg: String) {
        Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMsg(msg: String) {
        Toast.makeText(context, "Success: $msg", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoadingView() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun initPresenter(): BooksContract.Presenter = BooksPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createBookButton.setOnClickListener { view ->
            showCreateBookDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        //mPresenter?.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCreateBookDialog() {
        val bookView = View
            .inflate(context, R.layout.book_dialog, null)
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.book_create_dialog_title)
            .setView(bookView)
            .setCancelable(true)
            .setPositiveButton(R.string.book_create_confirm,
                DialogInterface.OnClickListener { _, _ ->
                    val newBook = Book(
                        isbn = bookView.findViewById<EditText>(R.id.bookIsbnEditText).text.toString(),
                        bookName = bookView.findViewById<EditText>(R.id.bookNameEditText).text.toString(),
                        author = bookView.findViewById<EditText>(R.id.bookAuthorEditText).text.toString(),
                        publishTime = bookView.findViewById<EditText>(R.id.bookDateEditText).text.toString()
                        )
                    mPresenter?.createBook(newBook)
                })
            .setNegativeButton(R.string.book_create_cancel, null)
            .create().show()

        showYearPicker(bookView)
    }

    override fun showBookUpdateDialog(book: Book) {
        val bookView = View
            .inflate(context, R.layout.book_dialog, null)
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.book_update_dialog_title)
            .setView(bookView)
            .setCancelable(true)
            .setPositiveButton(R.string.book_update_confirm,
                DialogInterface.OnClickListener {_, _ ->
                    mPresenter?.updateBook(getUpdateBook(bookView, book))
                })
            .setNegativeButton(R.string.book_update_cancel, null)
            .create().show()

        mPresenter?.bindBookDialogData(bookView, book, true)
        showYearPicker(bookView)
    }

    private fun getUpdateBook(bookView: View, book: Book): Book {
        return Book(
            id = book.id,
            isbn = bookView.findViewById<EditText>(R.id.bookIsbnEditText).text.toString(),
            bookName = bookView.findViewById<EditText>(R.id.bookNameEditText).text.toString(),
            author = bookView.findViewById<EditText>(R.id.bookAuthorEditText).text.toString(),
            publishTime = bookView.findViewById<EditText>(R.id.bookDateEditText).text.toString()
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showYearPicker(bookView: View) {
        val dateView = bookView.findViewById<EditText>(R.id.bookDateEditText)
        dateView.let {
            //just need date picker focus
            it.isFocusableInTouchMode = false
                it.setOnClickListener {
                    val calendar = Calendar.getInstance()

                    val year = calendar[Calendar.YEAR]
                    val monthOfYear = calendar[Calendar.MONTH]
                    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

                    val dateSetListener = DatePickerDialog.OnDateSetListener { view, selectedYear, _, _ ->
                        dateView.text = Editable.Factory.getInstance()
                            .newEditable("$selectedYear")
                    }

                    DatePickerDialog(
                        requireContext(),
                        dateSetListener,
                        year,
                        monthOfYear,
                        dayOfMonth
                    ).show()
                }
        }
    }

    override fun showBookDetail(book: Book) {
        val bookView = View
            .inflate(context, R.layout.book_dialog, null)
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.book_info_dialog_title)
            .setView(bookView)
            .setCancelable(true)
            .setPositiveButton(R.string.book_update_confirm, null)
            .create().show()

        mPresenter?.bindBookDialogData(bookView, book, false)
    }

    override fun showBookDeleteDialog(book: Book) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.delete_dialog_title)
            setMessage(R.string.delete_dialog_message)
            setPositiveButton(R.string.delete_dialog_confirm_button,
                DialogInterface.OnClickListener { _, _ ->
                    mPresenter?.deleteBook(book)
                })
            setNegativeButton(R.string.delete_dialog_cancel_button, null)
            create().show()
        }
    }

}