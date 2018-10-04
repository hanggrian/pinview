package com.hendraanggrian.appcompat.pinview.demo

import android.app.Application
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.hendraanggrian.appcompat.widget.PinGroup

@Suppress("unused")
class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.getDefaultSharedPreferences(this).run {
            if (!contains(PREFERENCE_COUNT)) {
                edit {
                    putString(PREFERENCE_COUNT, PinGroup.DEFAULT_COUNT.toString())
                }
            }
        }
    }
}