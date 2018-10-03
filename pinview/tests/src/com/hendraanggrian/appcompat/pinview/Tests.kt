package com.hendraanggrian.appcompat.pinview

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hendraanggrian.appcompat.pinview.test.R
import com.hendraanggrian.appcompat.widget.PinView
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

sealed class AbstractTest {

    abstract val text: String

    @Test fun setText() {
        onView(withId(R.id.pinView))
            .perform(object : ViewAction {
                override fun getDescription(): String = "set text"
                override fun getConstraints(): Matcher<View> = isAssignableFrom(PinView::class.java)
                override fun perform(uiController: UiController?, view: View) {
                    view as PinView
                    view.text = text
                    assertEquals(view.text, text)
                }
            })
    }
}

@RunWith(AndroidJUnit4::class)
class SimpleTest : AbstractTest() {

    override val text: String = "5462"

    @Rule @JvmField val activity = ActivityTestRule(SimpleActivity::class.java)
}

@RunWith(AndroidJUnit4::class)
class CustomTest : AbstractTest() {

    override val text: String = "934513"

    @Rule @JvmField val activity = ActivityTestRule(CustomActivity::class.java)
}