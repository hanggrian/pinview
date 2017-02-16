package io.github.hendraanggrian.pininputlayout.internal;

import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class PinInput extends EditText {

    public <Listener extends TextWatcher & OnKeyListener & OnFocusChangeListener> PinInput(Context context, Listener listener) {
        super(context);
        addTextChangedListener(listener);
        setOnKeyListener(listener);
        setOnFocusChangeListener(listener);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        setLayoutParams(params);

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void setTextAppearance(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setTextAppearance(resId);
        else
            super.setTextAppearance(getContext(), resId);
    }

    public void requestFocus(boolean moveCursorToEnd) {
        requestFocus();
        if (moveCursorToEnd)
            setSelection(getText().length());
    }

    public void setMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(left, top, right, bottom);
    }
}