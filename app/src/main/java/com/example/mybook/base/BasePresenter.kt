package com.example.mybook.base

open class BasePresenter<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {

    protected var mView: T? = null

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}
