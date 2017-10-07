package com.hendraanggrian.widget

import android.content.Context
import android.os.Build
import android.support.annotation.AnyRes
import android.support.annotation.ColorInt
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.hendraanggrian.pinedittext.R

open class PinEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_PIN_DIGITS = 4
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (onStateChangedListener != null) {
                if (areInputsFilled && !mComplete) {
                    mComplete = true
                    onStateChangedListener!!(this@PinEditText, true)
                } else if (!areInputsFilled && mComplete) {
                    mComplete = false
                    onStateChangedListener!!(this@PinEditText, false)
                }
            }
            if (onPinChangedListener != null) {
                val text = Array(pins.size) { "" }
                for (i in pins.indices) {
                    text[i] = pins[i].text.toString()
                }
                onPinChangedListener!!(this@PinEditText, text)
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (!s.toString().isEmpty() && mFocusedInput < pins.size - 1) {
                pins[mFocusedInput + 1].requestFocus()
            } else if (s.toString().isEmpty() && mFocusedInput > 0) {
                pins[mFocusedInput - 1].requestFocus()
            }
        }
    }
    private val mOnFocusedChangeListener: View.OnFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            mFocusedInput = pins.indexOf(v as Pin)
        }
    }

    private val mContainer: LinearLayout = LinearLayout(context)
    private var mFocusedInput: Int = 0
    private var mComplete: Boolean = false
    private var mDigits: Int
    private val mGap: Int
    @AnyRes private var mTextAppearance: Int
    @ColorInt private var mTextColor: Int
    private var mTextSize: Float

    open var onStateChangedListener: ((PinEditText, isComplete: Boolean) -> Unit)? = null
    open var onPinChangedListener: ((PinEditText, pins: Array<String>) -> Unit)? = null

    init {
        mContainer.orientation = LinearLayout.HORIZONTAL
        mContainer.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val a = context.obtainStyledAttributes(attrs, R.styleable.PinEditText, defStyleAttr, 0)
        mDigits = a.getInt(R.styleable.PinEditText_pinDigits, DEFAULT_PIN_DIGITS)
        mGap = a.getDimensionPixelSize(R.styleable.PinEditText_pinGap, context.resources.getDimensionPixelSize(R.dimen.margin_pin))
        mTextAppearance = a.getResourceId(R.styleable.PinEditText_pinTextAppearance,
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) android.R.style.TextAppearance_Material_Display1
                else android.R.style.TextAppearance_Large)
        mTextColor = a.getColor(R.styleable.PinEditText_pinTextColor, -1)
        mTextSize = a.getDimension(R.styleable.PinEditText_pinTextSize, -1f)

        (0 until mDigits).forEach {
            val pin = Pin(context)
            pin.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { weight = 1f }
            pin.addTextChangedListener(mTextWatcher)
            pin.onFocusChangeListener = mOnFocusedChangeListener
            (pin.layoutParams as LinearLayout.LayoutParams).setMargins(if (it > 0) mGap else 0, 0, 0, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pin.setTextAppearance(mTextAppearance)
            } else @Suppress("DEPRECATION") {
                pin.setTextAppearance(context, mTextAppearance)
            }
            if (mTextColor != -1) {
                pin.setTextColor(mTextColor)
            }
            if (mTextSize != -1f) {
                pin.textSize = mTextSize
            }
            mContainer.addView(pin)
        }
        a.recycle()

        addView(mContainer)
    }

    open val text: String
        get() {
            val sb = StringBuilder()
            forEachPin { sb.append(this) }
            return sb.toString()
        }

    fun clear() = forEachPin { setText("") }

    private val pins: List<Pin> get() = (0 until mContainer.childCount).map { mContainer.getChildAt(it) as Pin }
    private fun forEachPin(block: Pin.() -> Unit) = pins.forEach { block(it) }
    private val areInputsFilled: Boolean get() = pins.none { it.text.toString().isEmpty() }
}