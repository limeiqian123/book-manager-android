package com.example.mybook

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.lang.UCharacter.VerticalOrientation
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybook.adapter.BookAdapter
import com.example.mybook.data.Book
import com.example.mybook.databinding.FragmentBooksBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: BookAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        adapter = BookAdapter(requireContext())
        binding.bookRecycleView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        fetchDataFromServer()
        return binding.root
    }

    private fun fetchDataFromServer() {
        val bookList = listOf(
            Book("9787011234560", "book1", "Mike", "2020-02-01"),
            Book("9787011234890", "book2", "Joke", "2020-09-01"),
            Book("9787011234891", "book3", "Jake", "2020-11-01")
        )
        adapter.setData(bookList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createBookButton.setOnClickListener { view ->
            showCreateBookDialog()
        }
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
                    //todo
                })
            .setNegativeButton(R.string.book_create_cancel, null)
            .create().show()

        enableDatePicker(bookView)
    }

    private fun enableDatePicker(bookView: View) {
        val dateView = bookView.findViewById<EditText>(R.id.bookDateEditText)
        dateView.let {
            //just need date picker focus
            it.isFocusableInTouchMode = false
                it.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                        dateView.text = Editable.Factory.getInstance()
                            .newEditable("$year-${monthOfYear + 1}-$dayOfMonth")
                    }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
                        .show()
                }
        }
    }
}