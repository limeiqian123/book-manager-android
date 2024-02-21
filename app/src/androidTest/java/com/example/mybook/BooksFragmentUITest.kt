package com.example.mybook

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mybook.adapter.BookAdapter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BooksFragmentUITest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(MyCustomIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(MyCustomIdlingResource())
    }

    @Test
    fun testLaunchActivity() {
        onView(withText("Books List")).check(matches(isDisplayed()))
    }

    @Test
    fun testBookListUI() {
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        Thread.sleep(5000L)
        onView(withId(R.id.bookRecycleView))
            .check(matches(isDisplayed()))
            .perform(swipeUp())
            .perform(swipeDown())

    }

    @Test
    fun testShowBookDetailUI() {
        Thread.sleep(5000L)
        onView(withId(R.id.bookRecycleView))
            .perform(actionOnItemAtPosition<BookAdapter.BookViewHolder>
                (0, click()))
        Thread.sleep(5000L)
        onView(withText("Book detail")).check(matches(isDisplayed()))
    }

    @Test
    fun testAddBookUI() {
        onView(withId(R.id.createBookButton)).check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Create a book")).check(matches(isDisplayed()))
    }

    @Test
    fun testUpdateBookUI() {
        Thread.sleep(5000L)
        onView(withId(R.id.bookRecycleView))
            .perform(actionOnItemAtPosition<BookAdapter.BookViewHolder>
                (0, clickChildViewWithId(R.id.bookUpdateButton)))
        Thread.sleep(3000L)
        onView(withText("Update book")).check(matches(isDisplayed()))
    }

    @Test
    fun testDeleteBookUI() {
        Thread.sleep(5000L)
        onView(withId(R.id.bookRecycleView))
            .perform(scrollToPosition<BookAdapter.BookViewHolder>(6))
            .perform(actionOnItemAtPosition<BookAdapter.BookViewHolder>
                (6, clickChildViewWithId(R.id.bookDeleteButton)))
        Thread.sleep(2000L)
        onView(withText("Delete book")).check(matches(isDisplayed()))
    }

    private fun clickChildViewWithId(id: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "click child view with id $id"
        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }
}