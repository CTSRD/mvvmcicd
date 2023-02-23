package com.test.mvvmcicd


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CapClickTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun capClickTest() {
        val nodeItemView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.node_network_map),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        nodeItemView.perform(click())

        val extendedFloatingActionButton = onView(
            allOf(
                withId(R.id.tvDeviceIcon), withText("8"),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_device_info),
                        childAtPosition(
                            withId(R.id.bottom_sheet_info),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton.perform(click())

        val nodeItemView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.node_network_map),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        nodeItemView2.perform(click())

        val extendedFloatingActionButton2 = onView(
            allOf(
                withId(R.id.ivLocationIcon),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_location),
                        childAtPosition(
                            withId(R.id.bottom_sheet_info),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton2.perform(click())

        val nodeItemView3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.node_network_map),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        nodeItemView3.perform(click())

        val extendedFloatingActionButton3 = onView(
            allOf(
                withId(R.id.ivAddNodeIcon),
                childAtPosition(
                    allOf(
                        withId(R.id.cl_add_nodes),
                        childAtPosition(
                            withId(R.id.bottom_sheet_info),
                            6
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        extendedFloatingActionButton3.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
