package com.example.mybook

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
            .perform(actionWithAssertions(click()))
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

    }

    @Test
    fun testDeleteBookUI() {

    }
}