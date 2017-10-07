package com.hendraanggrian.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.v7.appcompat.R
import android.support.v7.widget.AppCompatEditText
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent

open class Pin(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_LENGTH = 1
    }

    private val mGestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (text.isNotEmpty()) {
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
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && text.isNotEmpty()) {
            setSelection(MAX_LENGTH)
        }
    }
}