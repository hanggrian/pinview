package com.hendraanggrian.app

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatDialog
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.hendraanggrian.pinedittext.R
import com.hendraanggrian.widget.PinEditText

open class PinDialog @JvmOverloads constructor(
        context: Context,
        theme: Int = R.style.PinEditTextDialog
) : AppCompatDialog(context, theme) {

    val editText: PinEditText
    val negativeButton: Button
    val positiveButton: Button

    private var mNegativeOnClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null
    private var mPositiveOnClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null

    init {
        setContentView(R.layout.pinedittext_dialog)
        editText = findViewById(R.id.editText)!!
        negativeButton = findViewById(R.id.negativeButton)!!
        positiveButton = findViewById(R.id.positiveButton)!!

        if (editText.childCount > 0) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
        editText.setOnStateChangedListener { _, isComplete -> positiveButton.isEnabled = isComplete }
        negativeButton.setOnClickListener {
            mNegativeOnClick?.invoke(this, DialogInterface.BUTTON_NEGATIVE, editText.text)
            hide()
        }
        positiveButton.setOnClickListener {
            mPositiveOnClick?.invoke(this, DialogInterface.BUTTON_POSITIVE, editText.text)
            hide()
        }
    }

    @JvmOverloads
    open fun setNegativeButton(text: CharSequence, onClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null) {
        negativeButton.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        negativeButton.text = text
        mNegativeOnClick = onClick
    }

    @JvmOverloads
    open fun setPositiveButton(text: CharSequence, onClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null) {
        positiveButton.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        positiveButton.text = text
        mPositiveOnClick = onClick
    }

    open class Builder constructor(
            val context: Context,
            val theme: Int = R.style.PinEditTextDialog
    ) {
        var title: CharSequence? = null
        var negativeButton: Pair<CharSequence, ((DialogInterface, Int, CharSequence) -> Unit)?>? = null
        var positiveButton: Pair<CharSequence, ((DialogInterface, Int, CharSequence) -> Unit)?>? = null

        open fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        @JvmOverloads
        open fun setNegativeButton(text: CharSequence, onClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null): Builder {
            negativeButton = Pair(text, onClick)
            return this
        }

        @JvmOverloads
        open fun setPositiveButton(text: CharSequence, onClick: ((DialogInterface, Int, CharSequence) -> Unit)? = null): Builder {
            positiveButton = Pair(text, onClick)
            return this
        }

        open fun build(): PinDialog {
            val dialog = PinDialog(context, theme)
            title?.let { dialog.setTitle(title) }
            negativeButton?.let { dialog.setNegativeButton(it.first, it.second) }
            positiveButton?.let { dialog.setPositiveButton(it.first, it.second) }
            return dialog
        }
    }
}