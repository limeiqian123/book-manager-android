package com.example.mybook.base

interface BaseContract {

    interface BaseView {
    }

    interface BasePresenter<in T> {
        fun attachView(view: T)
        fun detachView()
    }
}