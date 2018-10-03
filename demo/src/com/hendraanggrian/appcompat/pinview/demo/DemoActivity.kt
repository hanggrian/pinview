package com.hendraanggrian.appcompat.pinview.demo

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.widget.PinView
import com.hendraanggrian.bundler.Bundler
import com.hendraanggrian.bundler.Extra
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    private val stateListener = PinView.OnStateChangedListener { _, isComplete ->
        toolbar.title = when {
            isComplete -> "Complete"
            else -> "Enter your pin"
        }
    }
    private val preferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            when (key) {
                PREFERENCE_COUNT -> preferences.getString(key, null)?.let {
                    ConfirmDialogFragment()
                        .apply {
                            arguments = Bundler.extrasOf(ConfirmDialogFragment::class.java, it)
                        }
                        .show(supportFragmentManager, null)
                }
                PREFERENCE_GAP -> preferences.getString(key, null)?.toInt()?.let {
                    pinView.gap = it
                }
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

    class ConfirmDialogFragment : AppCompatDialogFragment() {
        @Extra lateinit var gap: String

        override fun onCreateDialog(state: Bundle?): Dialog = AlertDialog.Builder(context!!)
            .setTitle("Reset")
            .setMessage("Are you sure?")
            .setPositiveButton(android.R.string.yes) { _, _ ->
                Bundler.bindExtras(this)
                PreferenceManager.getDefaultSharedPreferences(context).edit {
                    putString(PREFERENCE_COUNT, gap)
                }
                ProcessPhoenix.triggerRebirth(context)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }
}