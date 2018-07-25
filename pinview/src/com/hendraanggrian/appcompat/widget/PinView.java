package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class PinView extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {

    public static final int DEFAULT_COUNT = 4;

    private OnStateChangedListener onStateChangedListener;
    private OnPinChangedListener onPinChangedListener;

    private int gap;
    private int focusedPin;
    private boolean isComplete;

    private final int pinCount;

    public PinView(@NonNull Context context) {
        this(context, null);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.pinViewStyle);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinView,
                defStyleAttr, R.style.Widget_PinView);
        pinCount = a.getInt(R.styleable.PinView_pinCount, DEFAULT_COUNT);
        for (int i = 0; i < pinCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.weight = 1;

            EditText view = new PinEditText(context);
            view.setLayoutParams(params);
            view.setOnFocusChangeListener(this);
            addView(view);
        }
        applyGap(a.getDimensionPixelSize(R.styleable.PinView_pinGap, 0));
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
        addTextChangedListener(this);

        a.recycle();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (getChildCount() > pinCount) {
            throw new IllegalStateException("Shouldn't modify PinView children.");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence text, int i, int i1, int i2) {
        if (onStateChangedListener != null) {
            if (isPinFilled() && !isComplete) {
                isComplete = true;
                onStateChangedListener.onComplete(this, true);
            } else if (!isPinFilled() && isComplete) {
                isComplete = false;
                onStateChangedListener.onComplete(this, false);
            }
        }
        if (onPinChangedListener != null) {
            onPinChangedListener.onPinChanged(this, getText());
        }
    }

    @Override
    public void afterTextChanged(Editable text) {
        if (!TextUtils.isEmpty(text) && focusedPin < getChilds().size() - 1) {
            getChilds().get(focusedPin + 1).requestFocus();
        } else if (TextUtils.isEmpty(text) && focusedPin > 0) {
            getChilds().get(focusedPin - 1).requestFocus();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            focusedPin = getChilds().indexOf(view);
        }
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        onStateChangedListener = listener;
    }

    public void setOnPinChangedListener(OnPinChangedListener listener) {
        onPinChangedListener = listener;
    }

    public void setGap(int gap) {
        this.gap = gap;
        applyGap(gap);
        onViewAdded(this);
    }

    public int getGap() {
        return gap;
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

    private void applyGap(int gap) {
        for (EditText view : getChilds()) {
            ((MarginLayoutParams) view.getLayoutParams())
                    .setMargins(gap / 2, 0, gap / 2, 0);
        }
    }

    public interface OnStateChangedListener {

        void onComplete(@NonNull PinView view, boolean isComplete);
    }

    public interface OnPinChangedListener {

        void onPinChanged(@NonNull PinView view, @NonNull CharSequence text);
    }
}