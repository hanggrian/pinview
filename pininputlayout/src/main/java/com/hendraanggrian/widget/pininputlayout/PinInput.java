package com.hendraanggrian.widget.pininputlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class PinInput extends AppCompatEditText {

    @NonNull private final GestureDetector gestureDetector;

    public <Listener extends TextWatcher & OnFocusChangeListener> PinInput(Context context, @NonNull Listener listener) {
        super(context);
        addTextChangedListener(listener);
        setOnFocusChangeListener(listener);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        setLayoutParams(params);

        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                setSelection(getText().length());
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public PinInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        throw new RuntimeException("PinInput isn't meant to be xml inflated.");
    }

    public void focus() {
        requestFocus();
        setSelection(getText().length());
    }

    public void setMargin(int left, int top, int right, int bottom) {
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(left, top, right, bottom);
    }
}