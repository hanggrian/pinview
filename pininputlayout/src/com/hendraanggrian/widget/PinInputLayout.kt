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
import android.widget.LinearLayout
import com.hendraanggrian.pininputlayout.R

open class PinInputLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_PIN_DIGITS = 4
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (onStateChangedListener != null) {
                if (areInputsFilled && !mComplete) {
                    mComplete = true
                    onStateChangedListener!!(this@PinInputLayout, true)
                } else if (!areInputsFilled && mComplete) {
                    mComplete = false
                    onStateChangedListener!!(this@PinInputLayout, false)
                }
            }
            if (onPinChangedListener != null) {
                val pins = Array(inputs.size) { "" }
                for (i in inputs.indices) {
                    pins[i] = inputs[i].text.toString()
                }
                onPinChangedListener!!(this@PinInputLayout, pins)
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (!s.toString().isEmpty() && mFocusedInput < inputs.size - 1) {
                inputs[mFocusedInput + 1].requestFocus()
            } else if (s.toString().isEmpty() && mFocusedInput > 0) {
                inputs[mFocusedInput - 1].requestFocus()
            }
        }
    }
    private val mOnFocusedChangeListener: View.OnFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            mFocusedInput = inputs.indexOf(v as PinInput)
        }
    }

    val inputs: List<PinInput>
    var onStateChangedListener: ((PinInputLayout, isComplete: Boolean) -> Unit)? = null
    var onPinChangedListener: ((PinInputLayout, pins: Array<String>) -> Unit)? = null

    private var mFocusedInput: Int = 0
    private var mComplete: Boolean = false
    private var mDigits: Int
    private val mGap: Int
    @AnyRes private var mTextAppearance: Int
    @ColorInt private var mTextColor: Int
    private var mTextSize: Float

    init {
        orientation = LinearLayout.HORIZONTAL

        val a = context.obtainStyledAttributes(attrs, R.styleable.PinInputLayout, defStyleAttr, 0)
        mDigits = a.getInt(R.styleable.PinInputLayout_pinDigits, DEFAULT_PIN_DIGITS)
        mGap = a.getDimensionPixelSize(R.styleable.PinInputLayout_pinGap, context.resources.getDimensionPixelSize(R.dimen.margin_pin))
        mTextAppearance = a.getResourceId(R.styleable.PinInputLayout_pinTextAppearance,
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) android.R.style.TextAppearance_Material_Display1
                else android.R.style.TextAppearance_Large)
        mTextColor = a.getColor(R.styleable.PinInputLayout_pinTextColor, -1)
        mTextSize = a.getDimension(R.styleable.PinInputLayout_pinTextSize, -1f)

        inputs = (0 until mDigits).map {
            val pin = PinInput(context)
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
            addView(pin)
            pin
        }
        a.recycle()
    }

    val text: String
        get() {
            var pin = ""
            forEachInput { pin += text }
            return pin
        }

    fun clear() = forEachInput { setText("") }

    private fun forEachInput(block: PinInput.() -> Unit) = inputs.forEach { block(it) }

    private val areInputsFilled: Boolean get() = inputs.none { it.text.toString().isEmpty() }
}