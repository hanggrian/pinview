package com.hendraanggrian.appcompat.pinview.demo

import android.content.Context
import android.graphics.Color
import com.hendraanggrian.appcompat.widget.PinView

class CustomPinView(context: Context) : PinView(context) {

    init {
        setBackgroundColor(Color.RED)
    }
}