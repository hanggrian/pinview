package com.hendraanggrian.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;

import com.hendraanggrian.appcompat.pinview.R;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.GestureDetectorCompat;

class PinEditText extends AppCompatEditText {

    private final GestureDetectorCompat gestureDetector;

    PinEditText(Context context) {
        super(context, null, R.attr.editTextStyle);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!TextUtils.isEmpty(getText())) {
                    setSelection(1);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && !TextUtils.isEmpty(getText())) {
            setSelection(1);
        }
    }
}