package com.example.mybook.adapter

import com.example.mybook.data.Book


interface ItemClickListener {
    fun onItemClick(book: Book)
    fun onUpdateButtonClick(book: Book)
    fun onDeleteButtonClick(book: Book)
}