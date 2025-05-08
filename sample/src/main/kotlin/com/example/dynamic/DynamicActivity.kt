package com.example.dynamic

import android.content.SharedPreferences
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.R
import com.hanggrian.pinview.PinView

class DynamicActivity : AppCompatActivity() {
    private val pinListener =
        PinView.OnPinChangedListener { _, pin ->
            fragment.findPreference<Preference>(PREFERENCE_TEXT)!!.summary = pin
        }
    private val stateListener =
        PinView.OnStateChangedListener { _, isComplete ->
            textView.text =
                when {
                    isComplete -> "Complete!"
                    else -> "You're almost there"
                }
        }
    private val preferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
            when (key) {
                PREFERENCE_TEXT -> p.getString(key, null)?.let { pinView.text = it }
                PREFERENCE_COUNT -> p.getString(key, null)?.toInt()?.let { pinView.count = it }
                PREFERENCE_GAP -> p.getString(key, null)?.toInt()?.let { pinView.gap = it }
            }
        }

    lateinit var toolbar: Toolbar
    lateinit var textView: TextView
    lateinit var pinView: PinView

    private lateinit var fragment: DynamicFragment
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(R.layout.activity_dynamic)
        toolbar = findViewById(R.id.toolbar)
        textView = findViewById(R.id.textView)
        pinView = findViewById(R.id.pinView)

        setSupportActionBar(toolbar)
        fragment = DynamicFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
        pinView.setOnPinChangedListener(pinListener)
        pinView.setOnStateChangedListener(stateListener)

        stateListener.onStateChanged(pinView, false)
        preferenceListener.onSharedPreferenceChanged(preferences, PREFERENCE_COUNT)
        preferenceListener.onSharedPreferenceChanged(preferences, PREFERENCE_GAP)

        WindowCompat
            .getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars =
            resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
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
