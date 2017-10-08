package com.hendraanggrian.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.AnyRes
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.hendraanggrian.pinedittext.R

open class PinEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = R.attr.pinEditTextStyle,
        @StyleRes defStyleRes: Int = R.style.Widget_PinEditText
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (mOnStateChangedListener != null) {
                if (arePinsFilled && !mComplete) {
                    mComplete = true
                    mOnStateChangedListener!!(this@PinEditText, true)
                } else if (!arePinsFilled && mComplete) {
                    mComplete = false
                    mOnStateChangedListener!!(this@PinEditText, false)
                }
            }
            mOnPinChangedListener?.invoke(this@PinEditText, text)
        }

        override fun afterTextChanged(s: Editable) {
            if (!s.toString().isEmpty() && mFocusedPin < mPins.size - 1) {
                mPins[mFocusedPin + 1].requestFocus()
            } else if (s.toString().isEmpty() && mFocusedPin > 0) {
                mPins[mFocusedPin - 1].requestFocus()
            }
        }

        private val arePinsFilled: Boolean get() = mPins.none { it.text.toString().isEmpty() }
    }
    private val mOnFocusedChangeListener: View.OnFocusChangeListener = OnFocusChangeListener { v, hasFocus -> if (hasFocus) mFocusedPin = mPins.indexOf(v as Pin) }
    private val mPinsContainer: LinearLayout = LinearLayout(context)
    private val mPins: List<Pin>
    private var mFocusedPin: Int = 0
    private var mComplete: Boolean = false

    private var mOnStateChangedListener: ((PinEditText, Boolean) -> Unit)? = null
    private var mOnPinChangedListener: ((PinEditText, String) -> Unit)? = null

    init {
        mPinsContainer.orientation = LinearLayout.HORIZONTAL
        mPinsContainer.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(mPinsContainer)

        val a = context.obtainStyledAttributes(attrs, R.styleable.PinEditText, defStyleAttr, defStyleRes)
        mPins = (0 until a.getInt(R.styleable.PinEditText_pinCount, 0))
                .map {
                    val pin = generatePin()
                    mPinsContainer.addView(pin)
                    pin
                }
        setGap(a.getDimensionPixelSize(R.styleable.PinEditText_pinGap, 0))
        if (a.hasValue(R.styleable.PinEditText_android_text)) {
            setText(a.getText(R.styleable.PinEditText_android_text))
        }
        setTextAppearance(a.getResourceId(R.styleable.PinEditText_android_textAppearance, 0))
        if (a.hasValue(R.styleable.PinEditText_android_textColor)) {
            setTextColor(a.getColorStateList(R.styleable.PinEditText_android_textColor))
        }
        if (a.hasValue(R.styleable.PinEditText_android_textSize)) {
            setTextSize(a.getDimension(R.styleable.PinEditText_android_textSize, 0f))
        }
        addTextChangedListener(mTextWatcher)
        a.recycle()
    }

    open fun setOnStateChangedListener(listener: ((view: PinEditText, isComplete: Boolean) -> Unit)?) {
        mOnStateChangedListener = listener
    }

    open fun setOnPinChangedListener(listener: ((view: PinEditText, text: String) -> Unit)?) {
        mOnPinChangedListener = listener
    }

    open val isComplete: Boolean get() = mComplete

    open val count: Int get() = mPins.count()

    open fun setGap(gap: Int) = forEachPin { (it.layoutParams as MarginLayoutParams).setMargins(gap / 2, 0, gap / 2, 0) }

    open fun setSelection(index: Int) = mPins[index].requestFocus()

    open val text: String
        get() {
            val sb = StringBuilder()
            forEachPin { sb.append(it.text) }
            return sb.toString()
        }

    open fun setText(text: CharSequence) = text.toString().toCharArray().forEachIndexed { index, c ->
        check(Character.isDigit(c), { "Text should be number only." })
        mPins[index].setText(c.toString())
    }

    open fun setTextAppearance(@AnyRes resId: Int) = forEachPin {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.setTextAppearance(resId)
        } else {
            @Suppress("DEPRECATION")
            it.setTextAppearance(context, resId)
        }
    }

    open fun setTextColor(@ColorInt color: Int) = setTextColor(ColorStateList.valueOf(color))
    open fun setTextColor(colors: ColorStateList) = forEachPin { it.setTextColor(colors) }

    open fun setTextSize(size: Float) = setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    open fun setTextSize(unit: Int, size: Float) = forEachPin { it.setTextSize(unit, size) }

    open fun addTextChangedListener(watcher: TextWatcher) = forEachPin { it.addTextChangedListener(watcher) }
    open fun removeTextChangedListener(watcher: TextWatcher) = forEachPin { it.removeTextChangedListener(watcher) }

    private fun generatePin(): Pin = Pin(context).apply {
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { weight = 1f }
        onFocusChangeListener = mOnFocusedChangeListener
    }

    private fun forEachPin(block: (Pin) -> Unit) = mPins.forEach { block(it) }
}