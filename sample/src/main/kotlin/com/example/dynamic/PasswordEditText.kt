package com.example.dynamic

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD
import com.hanggrian.pinview.PinEditText

class PasswordEditText(context: Context) : PinEditText(context) {
    init {
        inputType = TYPE_CLASS_NUMBER or TYPE_NUMBER_VARIATION_PASSWORD
        if (SDK_INT >= O) {
            setAutofillHints(AUTOFILL_HINT_PASSWORD)
        }
    }
}
