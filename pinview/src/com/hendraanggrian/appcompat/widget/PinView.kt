package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.hendraanggrian.appcompat.pinview.R

@Suppress("LeakingThis")
open class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.pinViewStyle,
    @StyleRes defStyleRes: Int = R.style.Widget_PinView
) : FrameLayout(context, attrs, defStyleAttr), TextWatcher, View.OnFocusChangeListener {

    private val arePinsFilled: Boolean get() = pinEditTexts.none { it.text.toString().isEmpty() }

    private val pinsContainer: LinearLayout = LinearLayout(context)
    private val pinEditTexts: List<PinEditText>
    private var focusedPin: Int = 0
    private var complete: Boolean = false

    private var mOnStateChangedListener: ((PinView, Boolean) -> Unit)? = null
    private var mOnPinChangedListener: ((PinView, String) -> Unit)? = null

    init {
        pinsContainer.orientation = LinearLayout.HORIZONTAL
        pinsContainer.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        addView(pinsContainer)

        val a = context.obtainStyledAttributes(attrs, R.styleable.PinView, defStyleAttr, defStyleRes)
        pinEditTexts = (0 until a.getInt(R.styleable.PinView_pinCount, 0))
            .map {
                val pin = generatePin()
                pinsContainer.addView(pin)
                pin
            }
        setGap(a.getDimensionPixelSize(R.styleable.PinView_pinGap, 0))
        if (a.hasValue(R.styleable.PinView_android_text)) {
            setText(a.getText(R.styleable.PinView_android_text))
        }
        setTextAppearance(a.getResourceId(R.styleable.PinView_android_textAppearance, 0))
        if (a.hasValue(R.styleable.PinView_android_textColor)) {
            setTextColor(a.getColorStateList(R.styleable.PinView_android_textColor))
        }
        if (a.hasValue(R.styleable.PinView_android_textSize)) {
            setTextSize(a.getDimension(R.styleable.PinView_android_textSize, 0f))
        }
        addTextChangedListener(this)
        a.recycle()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (mOnStateChangedListener != null) {
            if (arePinsFilled && !complete) {
                complete = true
                mOnStateChangedListener!!(this@PinView, true)
            } else if (!arePinsFilled && complete) {
                complete = false
                mOnStateChangedListener!!(this@PinView, false)
            }
        }
        mOnPinChangedListener?.invoke(this@PinView, text)
    }

    override fun afterTextChanged(s: Editable) {
        if (!s.toString().isEmpty() && focusedPin < pinEditTexts.size - 1) {
            pinEditTexts[focusedPin + 1].requestFocus()
        } else if (s.toString().isEmpty() && focusedPin > 0) {
            pinEditTexts[focusedPin - 1].requestFocus()
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            focusedPin = pinEditTexts.indexOf(view as PinEditText)
        }
    }

    open fun setOnStateChangedListener(listener: ((view: PinView, isComplete: Boolean) -> Unit)?) {
        mOnStateChangedListener = listener
    }

    open fun setOnPinChangedListener(listener: ((view: PinView, text: String) -> Unit)?) {
        mOnPinChangedListener = listener
    }

    open val isComplete: Boolean get() = complete

    open val count: Int get() = pinEditTexts.count()

    open fun setGap(gap: Int) = forEachPin { (it.layoutParams as MarginLayoutParams).setMargins(gap / 2, 0, gap / 2, 0) }

    open fun setSelection(index: Int) = pinEditTexts[index].requestFocus()

    open val text: String
        get() {
            val sb = StringBuilder()
            forEachPin { sb.append(it.text) }
            return sb.toString()
        }

    open fun setText(text: CharSequence) = text.toString().toCharArray().forEachIndexed { index, c ->
        check(Character.isDigit(c)) { "Text should be number only." }
        pinEditTexts[index].setText(c.toString())
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

    private fun generatePin(): PinEditText = PinEditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { weight = 1f }
        onFocusChangeListener = this@PinView
    }

    private fun forEachPin(block: (PinEditText) -> Unit) = pinEditTexts.forEach { block(it) }
}