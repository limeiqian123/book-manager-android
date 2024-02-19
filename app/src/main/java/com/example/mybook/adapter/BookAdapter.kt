package com.example.mybook.adapter

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.data.Book
import com.google.android.material.imageview.ShapeableImageView


class BookAdapter(): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var bookList: List<Book> = emptyList()
    private var listener: ItemClickListener? = null

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
        holder.bookNameText.text = bookList[position].bookName
        val book = bookList[holder.adapterPosition]

        holder.editButton.setOnClickListener {
            listener?.onUpdateButtonClick(book)
        }

        holder.deleteButton.setOnClickListener {
            listener?.onDeleteButtonClick(book)
        }

        holder.itemView.setOnClickListener {
            listener?.onItemClick(book)
        }
        // click book name can also pop up the detail dialog
        holder.bookNameText.setOnClickListener {
            listener?.onItemClick(book)
        }
    }
    fun setOnItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun getItemCount(): Int = bookList.size

    companion object {
        const val BOOK_NAME = "book_name"
        const val BOOK_ISBN = "book_isbn"
        const val BOOK_AUTHOR = "book_author"
        const val BOOK_DATE = "book_date"
    }
}

