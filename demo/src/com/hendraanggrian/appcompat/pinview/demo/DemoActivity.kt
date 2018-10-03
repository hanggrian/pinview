package com.hendraanggrian.appcompat.pinview.demo

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.widget.PinView
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    private val stateListener = PinView.OnStateChangedListener { _, isComplete ->
        toolbar.title = when {
            isComplete -> "Complete"
            else -> "Enter your pin"
        }
    }
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
        when (key) {
            PREFERENCE_COUNT -> p.getString(key, null)?.toInt()?.let { pinView.pinCount = it }
            PREFERENCE_GAP -> p.getString(key, null)?.toInt()?.let { pinView.pinGap = it }
        }
    }

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, DemoFragment())
            .commitNow()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        pinView.setOnStateChangedListener(stateListener)
        stateListener.onStateChanged(pinView, false)
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