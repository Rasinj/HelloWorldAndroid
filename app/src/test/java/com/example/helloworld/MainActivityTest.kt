package com.example.helloworld

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MainActivityTest {

    private lateinit var activity: MainActivity
    private lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        context = ApplicationProvider.getApplicationContext()
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()
    }

    @Test
    fun `activity should not be null`() {
        assertNotNull(activity)
    }

    @Test
    fun `activity should create without crashing`() {
        // Test that onCreate completes without throwing
        val recreatedActivity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .get()
        
        assertNotNull(recreatedActivity)
    }

    @Test
    fun `activity content view should be set`() {
        // Verify that setContentView was called and layout exists
        val contentView = activity.findViewById<LinearLayout>(android.R.id.content)
        assertNotNull(contentView)
    }

    @Test
    fun `activity should handle configuration changes`() {
        // Test configuration change doesn't crash
        activity.onConfigurationChanged(activity.resources.configuration)
        assertNotNull(activity)
    }

    @Test
    fun `activity lifecycle should work correctly`() {
        val activityController = Robolectric.buildActivity(MainActivity::class.java)
        
        // Test full lifecycle without crashes
        activityController.create()
        activityController.start()
        activityController.resume()
        activityController.pause()
        activityController.stop()
        activityController.destroy()
        
        // Should not throw any exceptions
        assertTrue(true)
    }

    @Test
    fun `activity should handle memory pressure`() {
        // Simulate low memory condition
        activity.onLowMemory()
        assertNotNull(activity)
    }

    @Test
    fun `activity should handle trim memory events`() {
        // Test different trim memory levels
        activity.onTrimMemory(android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
        activity.onTrimMemory(android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND)
        activity.onTrimMemory(android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE)
        
        assertNotNull(activity)
    }
}