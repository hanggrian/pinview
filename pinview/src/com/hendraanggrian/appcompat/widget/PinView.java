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
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.GestureDetectorCompat;

/**
 * View that represent single pin input. To modify, extend this view and use it in xml
 * (e.g.: {@code app:pinView="com.example.CustomPinView"}).
 *
 * @see PinGroup
 */
public class PinView extends AppCompatEditText {

    private final GestureDetectorCompat gestureDetector;

    public PinView(Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.weight = 1;

        setLayoutParams(params);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);

        GestureDetector.SimpleOnGestureListener gestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!TextUtils.isEmpty(getText())) {
                            setSelection(1);
                            return true;
                        }
                        return false;
                    }
                };
        gestureDetector = new GestureDetectorCompat(context, gestureListener);
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