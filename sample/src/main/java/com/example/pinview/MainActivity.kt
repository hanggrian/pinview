package com.example.pinview

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.pinview.widget.PinView
import com.jakewharton.processphoenix.ProcessPhoenix

class MainActivity : AppCompatActivity() {
    private val pinListener = PinView.OnPinChangedListener { _, pin ->
        fragment.findPreference<Preference>(PREFERENCE_TEXT)!!.summary = pin
    }
    private val stateListener = PinView.OnStateChangedListener { _, isComplete ->
        toolbar.title = when {
            isComplete -> "Complete"
            else -> "Enter your pin"
        }
    }
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
        when (key) {
            PREFERENCE_TEXT -> p.getString(key, null)?.let { pinView.text = it }
            PREFERENCE_COUNT -> p.getString(key, null)?.toInt()?.let { pinView.count = it }
            PREFERENCE_GAP -> p.getString(key, null)?.toInt()?.let { pinView.gap = it }
            PREFERENCE_LAYOUT -> Handler().postDelayed({ ProcessPhoenix.triggerRebirth(this) }, 500)
        }
    }

    lateinit var toolbar: Toolbar
    lateinit var pinView: PinView

    private lateinit var fragment: MainFragment
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        pinView = findViewById(R.id.pinView)

        setSupportActionBar(toolbar)
        fragment = MainFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
        pinView.setOnPinChangedListener(pinListener)
        pinView.setOnStateChangedListener(stateListener)

        stateListener.onStateChanged(pinView, false)
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
