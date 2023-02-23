package com.test.mvvmcicd

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Predicate
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.test.mvvmcicd.model.NodeModel
import com.test.mvvmcicd.utils.NodeItemView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class NewNetworkMapViewModelTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val viewModel: NewNetworkMapViewModel? = null

    private val repos: MutableLiveData<HashMap<String, NodeModel>> =
        MutableLiveData<HashMap<String, NodeModel>>()

    @Before
    fun init(){
    }

    @Test
    fun getNetworkMapTest(){
        val nodeItemView = onView(withId(R.id.node_network_map)).check(
            matches(
                withViewCount(instanceOf(NodeItemView::class.java), 3)
            )
        )
    }

    @Test
    fun getNodeListTest(){
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

    fun withViewCount(viewMatcher: Matcher<View>, expectedCount: Int): Matcher<View?>? {
        return object : TypeSafeMatcher<View?>() {
            var actualCount = -1
            override fun describeTo(description: Description) {
                if (actualCount >= 0) {
                    description.appendText("With expected number of items: $expectedCount")
                    description.appendText("\n With matcher: ")
                    viewMatcher.describeTo(description)
                    description.appendText("\n But got: $actualCount")
                }
            }

            override fun matchesSafely(root: View?): Boolean {
                actualCount = 0
                actualCount = TreeIterables.breadthFirstViewTraversal(root).count {
                    viewMatcher.matches(it)
                }
                return actualCount == expectedCount
            }
        }
    }

    private fun withMatcherPredicate(matcher: Matcher<View>): Predicate<View?>? {
        return Predicate<View?> { view -> matcher.matches(view) }
    }
}