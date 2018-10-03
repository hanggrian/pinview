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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hendraanggrian.appcompat.pinview.R;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.util.Consumer;
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
            if (!TextUtils.isEmpty(s) && focusedPin < getCount() - 1) {
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

    public void setCount(int count) {
        final int diff = count - getCount();
        if (diff < 0) {
            removeViews(count, -diff);
        } else if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                TextView view = new PinEditText(getContext());
                view.setOnFocusChangeListener(focusListener);
                view.addTextChangedListener(textWatcher);
                addView(view);
            }
        }
    }

    public int getCount() {
        return getChildCount();
    }

    public void setGap(@Px int gap) {
        pinGap = gap;
        applyGap();
    }

    @Px
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
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                builder.append(textView.getText());
            }
        });
        return builder.toString();
    }

    public void setTextAppearance(@StyleRes final int res) {
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                if (Build.VERSION.SDK_INT >= 23) {
                    textView.setTextAppearance(res);
                } else {
                    TextViewCompat.setTextAppearance(textView, res);
                }
            }
        });
    }

    public void setTextColor(@ColorInt final int color) {
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                textView.setTextColor(color);
            }
        });
    }

    public void setTextColor(@Nullable final ColorStateList colors) {
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                textView.setTextColor(colors);
            }
        });
    }

    public void setTextSize(final float size) {
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                textView.setTextSize(size);
            }
        });
    }

    public void setTextSize(final int unit, final float size) {
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView textView) {
                textView.setTextSize(unit, size);
            }
        });
    }

    public void setOnPinChangedListener(@Nullable OnPinChangedListener listener) {
        pinListener = listener;
    }

    public void setOnStateChangedListener(@Nullable OnStateChangedListener listener) {
        stateListener = listener;
    }

    private void applyGap() {
        if (pinGap > 0 && getCount() > 1) {
            forEach(new Consumer<TextView>() {
                @Override
                public void accept(TextView textView) {
                    ((MarginLayoutParams) textView.getLayoutParams()).setMargins(
                            pinGap / 2,
                            0,
                            pinGap / 2,
                            0
                    );
                }
            });
        }
    }

    private boolean isPinFilled() {
        for (int i = 0; i < getCount(); i++) {
            if (TextUtils.isEmpty(getPinAt(i).getText())) {
                return false;
            }
        }
        return true;
    }

    private void forEach(Consumer<TextView> consumer) {
        for (int i = 0; i < getCount(); i++) {
            consumer.accept(getPinAt(i));
        }
    }

    public interface OnPinChangedListener {

        void onPinChanged(@NonNull PinView view, @NonNull CharSequence s);
    }

    public interface OnStateChangedListener {

        void onStateChanged(@NonNull PinView view, boolean isComplete);
    }
}