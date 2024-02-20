package com.example.mybook

import androidx.test.espresso.IdlingResource

class MyCustomIdlingResource : IdlingResource {

    override fun getName(): String {
        return "my custom idling resource"
    }

    override fun isIdleNow(): Boolean {
        //to check if is bench
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {}
}