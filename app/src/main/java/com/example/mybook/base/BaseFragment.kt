package com.example.mybook.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment<in V: BaseContract.BaseView, P: BaseContract.BasePresenter<V>> : Fragment(){

    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter!!.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.detachView()
    }

    protected abstract fun initPresenter(): P
}
