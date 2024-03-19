package com.wradchuk.forgithub

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.wradchuk.forgithub.test_navigation.TestNavigationActivity
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class NavigationUITest {

    @get:Rule
    var activityRule = ActivityScenarioRule(TestNavigationActivity::class.java)

    /**
     * Тестирует функции навигации, упадёт если поменять методы местами так как идёт подсчёт
     * количества экранов внутри стека
     */
    @Test
    fun allTest() {
        testAddRootScreen()
        testReplaceRootFragment()
        testAddFragmentAfterReplaceRoot()
        testAddFragmentAfterAdd()
        testBackToFirstAlsoBackToReplacedFragmentAfterAddRoot()
        testAddFragmentAfterBackTo()
        testBackToPrevFragment()
        testExit()
    }

    /**
     * Добавить новый экран как стартовый
     */
    private fun testAddRootScreen() {
        onView(withId(R.id.addRootScreen)).perform(click())
        assertSizeBackstack(1)
    }
    /**
     * Заменить стартовый новым
     */
    private fun testReplaceRootFragment() {
        onView(withId(R.id.replaceFragment)).perform(click())
        assertSizeBackstack(1)
    }
    /**
     * Добавить новый экран поверх заменённого
     */
    private fun testAddFragmentAfterReplaceRoot() {
        onView(withId(R.id.addFragment)).perform(click())
        assertSizeBackstack(2)
    }
    /**
     * Добавить новый экран поверх заменённого
     */
    private fun testAddFragmentAfterAdd() {
        onView(withId(R.id.addFragment)).perform(click())
        assertSizeBackstack(3)
    }
    /**
     * Вернутся к указанному экрану
     */
    private fun testBackToFirstAlsoBackToReplacedFragmentAfterAddRoot() {
        onView(withId(R.id.backTo)).perform(click())
        assertSizeBackstack(1)
    }
    /**
     * Добавить новый экран поверх заменённого
     */
    private fun testAddFragmentAfterBackTo() {
        onView(withId(R.id.addFragment)).perform(click())
        assertSizeBackstack(2)
    }
    /**
     * Вернутся на экран который был до открытого сейчас
     */
    private fun testBackToPrevFragment() {
        onView(withId(R.id.back)).perform(click())
        assertSizeBackstack(1)
    }
    /**
     * Закрыть все экраны и завершить активность
     */
    private fun testExit() {
        onView(withId(R.id.exit)).perform(click())
    }
    /**
     * Проверка что в бэкстеке количество экранов соответствует заданной логике открытия и закрытия
     */
    private fun assertSizeBackstack(expected: Int) {
        activityRule.scenario.onActivity {
            it?.let { activity ->

                val customNavigator = activity.customNavigator

                val actual = customNavigator.getScreenBackStack().size
                assertEquals(expected, actual)
            }
        }
    }
}