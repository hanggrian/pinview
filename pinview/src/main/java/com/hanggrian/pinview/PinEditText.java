package com.hanggrian.pinview;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * View that represent single pin input. To modify, extend this view and use it in xml
 * (e.g.: {@code app:pinView="com.example.CustomPinView"}).
 */
public class PinEditText extends AppCompatEditText {
    private final GestureDetector gestureDetector;

    public PinEditText(Context context) {
        super(context);

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.weight = 1;
        setLayoutParams(params);

        setInputType(TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        setGravity(Gravity.CENTER_HORIZONTAL);

        gestureDetector =
            new GestureDetector(
                context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                        if (!TextUtils.isEmpty(getText())) {
                            setSelection(1);
                            return true;
                        }
                        return false;
                    }
                }
            );
    }

    @Override
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
