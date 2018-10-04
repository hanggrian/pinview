package com.hendraanggrian.appcompat.pinview.demo

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.widget.PinGroup
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    private val pinListener = PinGroup.OnPinChangedListener { _, pin ->
        fragment.findPreference(PREFERENCE_TEXT).summary = pin
    }
    private val stateListener = PinGroup.OnStateChangedListener { _, isComplete ->
        toolbar.title = when {
            isComplete -> "Complete"
            else -> "Enter your pin"
        }
    }
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
        when (key) {
            PREFERENCE_TEXT -> p.getString(key, null)?.let { pinGroup.text = it }
            PREFERENCE_COUNT -> p.getString(key, null)?.toInt()?.let { pinGroup.count = it }
            PREFERENCE_GAP -> p.getString(key, null)?.toInt()?.let { pinGroup.gap = it }
            PREFERENCE_LAYOUT -> Handler().postDelayed({ ProcessPhoenix.triggerRebirth(this) }, 500)
        }
    }

    private lateinit var fragment: DemoFragment
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(
            when {
                preferences.getBoolean(PREFERENCE_LAYOUT, false) -> R.layout.activity_demo2
                else -> R.layout.activity_demo
            }
        )
        setSupportActionBar(toolbar)
        fragment = DemoFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
        pinGroup.setOnPinChangedListener(pinListener)
        pinGroup.setOnStateChangedListener(stateListener)

        stateListener.onStateChanged(pinGroup, false)
        preferenceListener.onSharedPreferenceChanged(preferences, PREFERENCE_COUNT)
        preferenceListener.onSharedPreferenceChanged(preferences, PREFERENCE_GAP)
    }

    override fun onResume() {
        super.onResume()
        preferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    override fun onPause() {
        super.onPause()
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
    }
}