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

import com.hendraanggrian.appcompat.pinview.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.widget.TextViewCompat;

public class PinView extends LinearLayout {

    public static final int DEFAULT_COUNT = 4;

    @SuppressWarnings("FieldCanBeLocal")
    private final TextWatcher textListener = new TextWatcher() {
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
            if (!TextUtils.isEmpty(s) && focusedPin < getChilds().size() - 1) {
                getChilds().get(focusedPin + 1).requestFocus();
            } else if (TextUtils.isEmpty(s) && focusedPin > 0) {
                getChilds().get(focusedPin - 1).requestFocus();
            }
        }
    };

    @SuppressWarnings({"FieldCanBeLocal", "SuspiciousMethodCalls"})
    private final OnFocusChangeListener focusListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                focusedPin = getChilds().indexOf(view);
            }
        }
    };

    private OnStateChangedListener stateListener;
    private OnPinChangedListener pinListener;

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
        setPinCount(a.getInt(R.styleable.PinView_pinCount, DEFAULT_COUNT));
        setPinGap(a.getDimensionPixelSize(R.styleable.PinView_pinGap, 0));
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
        addTextChangedListener(textListener);

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

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        stateListener = listener;
    }

    public void setOnPinChangedListener(OnPinChangedListener listener) {
        pinListener = listener;
    }

    public void setPinCount(int count) {
        if (getPinCount() > count) {
            removeViews(count, getPinCount() - count);
        } else if (getPinCount() < count) {
            for (int i = 0; i < count - getPinCount(); i++) {
                EditText view = new PinEditText(getContext());
                view.setOnFocusChangeListener(focusListener);
                addView(view);
            }
        }
    }

    public int getPinCount() {
        return getChildCount();
    }

    public void setPinGap(int gap) {
        pinGap = gap;
        applyGap();
    }

    public int getPinGap() {
        return pinGap;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setSelection(int index) {
        getChilds().get(index).requestFocus();
    }

    public void setText(@NonNull CharSequence text) {
        final char[] pins = text.toString().toCharArray();
        for (int i = 0; i < pins.length; i++) {
            if (!Character.isDigit(pins[i])) {
                throw new IllegalStateException("Text should be digits.");
            }
            getChilds().get(i).setText(String.valueOf(pins[i]));
        }
    }

    public void setText(@StringRes int res) {
        setText(getResources().getText(res));
    }

    @NonNull
    public CharSequence getText() {
        final StringBuilder builder = new StringBuilder();
        for (EditText view : getChilds()) {
            builder.append(view.getText());
        }
        return builder.toString();
    }

    public void setTextAppearance(@StyleRes int res) {
        for (EditText view : getChilds()) {
            TextViewCompat.setTextAppearance(view, res);
        }
    }

    public void setTextColor(@ColorInt int color) {
        for (EditText view : getChilds()) {
            view.setTextColor(color);
        }
    }

    public void setTextColor(ColorStateList colors) {
        for (EditText view : getChilds()) {
            view.setTextColor(colors);
        }
    }

    public void setTextSize(float size) {
        for (EditText view : getChilds()) {
            view.setTextSize(size);
        }
    }

    public void setTextSize(int unit, float size) {
        for (EditText view : getChilds()) {
            view.setTextSize(unit, size);
        }
    }

    public void addTextChangedListener(@Nullable TextWatcher watcher) {
        for (EditText view : getChilds()) {
            view.addTextChangedListener(watcher);
        }
    }

    public void removeTextChangedListener(@Nullable TextWatcher watcher) {
        for (EditText view : getChilds()) {
            view.removeTextChangedListener(watcher);
        }
    }

    private boolean isPinFilled() {
        for (EditText view : getChilds()) {
            if (TextUtils.isEmpty(view.getText())) {
                return false;
            }
        }
        return true;
    }

    private List<EditText> getChilds() {
        final List<EditText> views = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            views.add((EditText) getChildAt(i));
        }
        return views;
    }

    public interface OnStateChangedListener {

        void onStateChanged(@NonNull PinView view, boolean isComplete);
    }

    public interface OnPinChangedListener {

        void onPinChanged(@NonNull PinView view, @NonNull CharSequence s);
    }
}