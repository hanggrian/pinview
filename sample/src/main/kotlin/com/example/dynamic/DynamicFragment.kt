package com.example.dynamic

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.example.R

class DynamicFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.fragment_dynamic)
        find<EditTextPreference>(PREFERENCE_TEXT).bindSummary({ text })
        find<EditTextPreference>(PREFERENCE_COUNT).bindSummary({ text })
        find<EditTextPreference>(PREFERENCE_GAP).bindSummary({ text })
    }

    private fun <T : Preference> find(key: CharSequence): T = findPreference<T>(key) as T

    private inline fun <T : Preference> find(key: CharSequence, block: T.() -> Unit): T =
        find<T>(key).apply(block)

    /**
     * @param initial starting value can be obtained from its value, text, etc.
     * @param convert its preference value to representable summary text.
     */
    private fun <P : Preference, T> P.bindSummary(
        initial: P.() -> T?,
        convert: (T) -> CharSequence? = { it?.toString() },
    ) {
        initial()?.let { summary = convert(it) }
        onPreferenceChangeListener =
            OnPreferenceChangeListener { preference, newValue ->
                @Suppress("UNCHECKED_CAST")
                preference.summary = convert(newValue as T)
                true
            }
    }
}
