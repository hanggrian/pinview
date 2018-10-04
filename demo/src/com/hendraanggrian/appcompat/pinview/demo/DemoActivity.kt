package com.hendraanggrian.appcompat.pinview.demo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.widget.PinGroup
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    private val pinListener = PinGroup.OnPinChangedListener { _, pin -> pinMenu?.title = pin }
    private val stateListener = PinGroup.OnStateChangedListener { _, isComplete ->
        toolbar.title = when {
            isComplete -> "Complete"
            else -> "Enter your pin"
        }
    }
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
        when (key) {
            PREFERENCE_COUNT -> p.getString(key, null)?.toInt()?.let { pinGroup.count = it }
            PREFERENCE_GAP -> p.getString(key, null)?.toInt()?.let { pinGroup.gap = it }
        }
    }

    private var pinMenu: MenuItem? = null
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_demo, menu)
        pinMenu = menu.findItem(R.id.pin)
        return true
    }
}