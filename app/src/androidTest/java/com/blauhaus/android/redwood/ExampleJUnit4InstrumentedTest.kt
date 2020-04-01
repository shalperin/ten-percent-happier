package com.blauhaus.android.redwood

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.blauhaus.android.redwood.app.MainActivity
import com.blauhaus.android.redwood.app.R
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.junit.MockitoJUnit.rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val tag = "app-espresso"

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.blauhaus.android.redwood", appContext.packageName)
    }

    @Test
    fun goHome() {
        try {
            clickOn(R.id.go_home)
        } catch(e: Exception) {}
        finally {
            assertDisplayed(R.id.home)
        }
    }

    @Test
    fun testAccessDeniedRedirect() {
        goHome()

        try {
            clickOn(R.string.logout_button_text)
        } catch (e: Exception) {
            Log.d(tag, "I wasn't logged in.")
        } finally {
            clickOn(R.id.launch_todo)
            assertDisplayed(R.id.please_login)
        }
    }

    @Test
    fun attemptRouteNotRequiringLogin() {
        goHome()
        clickOn(R.id.launch_meditation)
        assertDisplayed(R.id.meditation_challenge)
    }


 }
