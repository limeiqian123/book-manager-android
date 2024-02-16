package com.example.mybook.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.data.Book
import com.example.mybook.databinding.BookItemBinding
import com.google.android.material.imageview.ShapeableImageView


class BookAdapter(private val context: Context): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var _binding: BookItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookList: List<Book>

    @SuppressLint("NotifyDataSetChanged")
    fun setData(bookList: List<Book>) {
        this.bookList = bookList
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookNameText: TextView = itemView.findViewById(R.id.bookName)
        val deleteButton: ShapeableImageView = itemView.findViewById(R.id.bookDeleteButton)
        val editButton: ShapeableImageView = itemView.findViewById(R.id.bookUpdateButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookNameText.text = bookList[position].name
        val book = bookList[holder.adapterPosition]
        setDeleteClickListener(holder.deleteButton, book)
        setEditClickListener(holder.editButton, book)
        holder.itemView.setOnClickListener {
            showBookDialog(book, R.string.book_info_dialog_title, false)
        }
        holder.bookNameText.setOnClickListener {
            showBookDialog(book, R.string.book_info_dialog_title, false)
        }
    }

    private fun setDeleteClickListener(view: ShapeableImageView, book: Book) {
        view.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun setEditClickListener(view: ShapeableImageView, book: Book) {
        view.setOnClickListener {
            showBookDialog(book, R.string.book_update_dialog_title)
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context).apply {
            setTitle(R.string.delete_dialog_title)
            setMessage(R.string.delete_dialog_message)
            setPositiveButton(R.string.delete_dialog_confirm_button,
                DialogInterface.OnClickListener { _, _ ->
                    //todo
            })
            setNegativeButton(R.string.delete_dialog_cancel_button, null)
            create().show()
        }
    }

    private fun showBookInfoDialog(book: Book) {
        val bookItems = arrayOf(book.name, book.isbn, book.author, book.date)
        AlertDialog.Builder(context)
            .setTitle(R.string.book_info_dialog_title) // 设置对话框标题
            .setItems(bookItems) { _, _ -> }
            .show()
    }

    private fun showBookDialog(book: Book, titleRes: Int, editable: Boolean = true) {
        val bookView = View
            .inflate(context, R.layout.book_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setTitle(titleRes)
            .setView(bookView)
            .setCancelable(true)

        if (editable) {
            builder.setPositiveButton(R.string.book_update_confirm,
                DialogInterface.OnClickListener {_, _ ->
                    //todo
                })
                .setNegativeButton(R.string.book_update_cancel, null)
        } else {
            builder.setPositiveButton(R.string.book_update_confirm, null)
        }

        builder.create().show()

        bindData(bookView, book, editable)
    }

    private fun bindData(bookView: View, book: Book, editable: Boolean) {
        val dateEditText = bookView.findViewById<EditText>(R.id.bookDateEditText)
        val nameEditText = bookView.findViewById<EditText>(R.id.bookNameEditText)
        val isbnEditText = bookView.findViewById<EditText>(R.id.bookIsbnEditText)
        val authorEditText = bookView.findViewById<EditText>(R.id.bookAuthorEditText)
        dateEditText.apply {
            setText(book.date)
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
            text = Editable.Factory.getInstance().newEditable(book.name)
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


    override fun getItemCount(): Int = bookList.size

    companion object {
        const val BOOK_NAME = "book_name"
        const val BOOK_ISBN = "book_isbn"
        const val BOOK_AUTHOR = "book_author"
        const val BOOK_DATE = "book_date"
    }
}