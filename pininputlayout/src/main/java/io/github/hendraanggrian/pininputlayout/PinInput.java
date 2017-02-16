package io.github.hendraanggrian.pininputlayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class PinInput extends EditText {

    <Listener extends TextWatcher & OnFocusChangeListener> PinInput(Context context, @NonNull Listener listener) {
        super(context);
        addTextChangedListener(listener);
        setOnFocusChangeListener(listener);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        setLayoutParams(params);

        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public PinInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinInput(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PinInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        throw new RuntimeException("PinInput isn't meant to be xml inflated.");
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setTextAppearance(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setTextAppearance(resId);
        else
            super.setTextAppearance(getContext(), resId);
    }

    void requestFocus(boolean moveCursorToEnd) {
        requestFocus();
        if (moveCursorToEnd)
            setSelection(getText().length());
    }

    void setMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(left, top, right, bottom);
    }
}