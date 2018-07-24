package com.hendraanggrian.appcompat.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.hendraanggrian.appcompat.pinview.R

internal class PinEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private companion object {
        const val MAX_LENGTH = 1
    }

    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (text!!.isNotEmpty()) {
                    setSelection(MAX_LENGTH)
                    return true
                }
                return false
            }
        })

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))
        gravity = Gravity.CENTER_HORIZONTAL
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && text!!.isNotEmpty()) setSelection(MAX_LENGTH)
    }
}