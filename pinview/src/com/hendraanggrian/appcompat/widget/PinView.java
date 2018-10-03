package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hendraanggrian.appcompat.pinview.R;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.widget.TextViewCompat;

public class PinView extends LinearLayout {

    public static final int DEFAULT_COUNT = 4;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (stateListener != null) {
                if (isPinFilled() && !isComplete) {
                    isComplete = true;
                    stateListener.onStateChanged(PinView.this, true);
                } else if (!isPinFilled() && isComplete) {
                    isComplete = false;
                    stateListener.onStateChanged(PinView.this, false);
                }
            }
            if (pinListener != null) {
                pinListener.onPinChanged(PinView.this, getText());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s) && focusedPin < getChildCount() - 1) {
                getPinAt(focusedPin + 1).requestFocus();
            } else if (TextUtils.isEmpty(s) && focusedPin > 0) {
                getPinAt(focusedPin - 1).requestFocus();
            }
        }
    };
    private final OnFocusChangeListener focusListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                focusedPin = indexOfChild(view);
            }
        }
    };

    private OnPinChangedListener pinListener;
    private OnStateChangedListener stateListener;

    private int pinGap;
    private int focusedPin;
    private boolean isComplete;

    public PinView(@NonNull Context context) {
        this(context, null);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.pinViewStyle);
    }

    public PinView(
        @NonNull Context context,
        @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(
            attrs,
            R.styleable.PinView,
            defStyleAttr,
            R.style.Widget_PinView
        );
        setCount(a.getInt(R.styleable.PinView_pinCount, DEFAULT_COUNT));
        setGap(a.getDimensionPixelSize(R.styleable.PinView_pinGap, 0));
        if (a.hasValue(R.styleable.PinView_android_text)) {
            setText(a.getText(R.styleable.PinView_android_text));
        }
        setTextAppearance(a.getResourceId(R.styleable.PinView_android_textAppearance, 0));
        if (a.hasValue(R.styleable.PinView_android_textColor)) {
            setTextColor(a.getColorStateList(R.styleable.PinView_android_textColor));
        }
        if (a.hasValue(R.styleable.PinView_android_textSize)) {
            setTextSize(a.getDimension(R.styleable.PinView_android_textSize, 0f));
        }
        a.recycle();
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation == LinearLayout.VERTICAL) {
            throw new UnsupportedOperationException("Vertical pins are not yet supported");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        applyGap();
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        applyGap();
    }

    @NonNull
    public TextView getPinAt(int index) {
        return (PinEditText) getChildAt(index);
    }

    private void applyGap() {
        if (pinGap > 0 && getChildCount() > 1) {
            for (int i = 0; i < getChildCount(); i++) {
                final MarginLayoutParams lp = (MarginLayoutParams) getChildAt(i).getLayoutParams();
                final int gapStart = i == 0 ? 0 : pinGap / 2;
                final int gapEnd = i == getChildCount() - 1 ? 0 : pinGap / 2;
                if (Build.VERSION.SDK_INT >= 17) {
                    lp.setMarginStart(gapStart);
                    lp.setMarginEnd(gapEnd);
                } else {
                    lp.setMargins(gapStart, 0, gapEnd, 0);
                }
            }
        }
    }

    public void setCount(int count) {
        if (getChildCount() > count) {
            removeViews(count, getChildCount() - count);
        } else if (getChildCount() < count) {
            for (int i = 0; i < count - getChildCount(); i++) {
                EditText view = new PinEditText(getContext());
                view.setOnFocusChangeListener(focusListener);
                view.addTextChangedListener(textWatcher);
                addView(view);
            }
        }
    }

    public int getCount() {
        return getChildCount();
    }

    public void setGap(int gap) {
        pinGap = gap;
        applyGap();
    }

    public int getGap() {
        return pinGap;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setSelection(int index) {
        getPinAt(index).requestFocus();
    }

    public void setText(@NonNull CharSequence text) {
        final char[] pins = text.toString().toCharArray();
        for (int i = 0; i < pins.length; i++) {
            if (!Character.isDigit(pins[i])) {
                throw new IllegalStateException("Text should be digits");
            }
            getPinAt(i).setText(String.valueOf(pins[i]));
        }
    }

    public void setText(@StringRes int res) {
        setText(getResources().getText(res));
    }

    @NonNull
    public CharSequence getText() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            builder.append(getPinAt(i).getText());
        }
        return builder.toString();
    }

    public void setTextAppearance(@StyleRes int res) {
        for (int i = 0; i < getChildCount(); i++) {
            TextViewCompat.setTextAppearance(getPinAt(i), res);
        }
    }

    public void setTextColor(@ColorInt int color) {
        for (int i = 0; i < getChildCount(); i++) {
            getPinAt(i).setTextColor(color);
        }
    }

    public void setTextColor(@Nullable ColorStateList colors) {
        for (int i = 0; i < getChildCount(); i++) {
            getPinAt(i).setTextColor(colors);
        }
    }

    public void setTextSize(float size) {
        for (int i = 0; i < getChildCount(); i++) {
            getPinAt(i).setTextSize(size);
        }
    }

    public void setTextSize(int unit, float size) {
        for (int i = 0; i < getChildCount(); i++) {
            getPinAt(i).setTextSize(unit, size);
        }
    }

    public void setOnPinChangedListener(@Nullable OnPinChangedListener listener) {
        pinListener = listener;
    }

    public void setOnStateChangedListener(@Nullable OnStateChangedListener listener) {
        stateListener = listener;
    }

    private boolean isPinFilled() {
        for (int i = 0; i < getChildCount(); i++) {
            if (TextUtils.isEmpty(getPinAt(i).getText())) {
                return false;
            }
        }
        return true;
    }

    public interface OnPinChangedListener {

        void onPinChanged(@NonNull PinView view, @NonNull CharSequence s);
    }

    public interface OnStateChangedListener {

        void onStateChanged(@NonNull PinView view, boolean isComplete);
    }
}