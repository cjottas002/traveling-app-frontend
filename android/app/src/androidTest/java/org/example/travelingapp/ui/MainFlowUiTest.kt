package org.example.travelingapp.ui

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFlowUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity_launchesWithoutCrash() {
        assertTrue(
            activityRule.scenario.state in setOf(
                Lifecycle.State.CREATED,
                Lifecycle.State.STARTED,
                Lifecycle.State.RESUMED
            )
        )
        activityRule.scenario.onActivity { activity ->
            assertFalse(activity.isFinishing)
            assertFalse(activity.isDestroyed)
        }
    }

    @Test
    fun mainActivity_canBeClosedCleanly() {
        activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)
        assertEquals(Lifecycle.State.DESTROYED, activityRule.scenario.state)
    }
}
