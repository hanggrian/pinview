package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
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
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;

/**
 * Container of {@link PinView} that has been modified to operate like {@link TextView}.
 */
public class PinGroup extends LinearLayout {

    /**
     * Default pin count used when unspecified by user.
     */
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
                    stateListener.onStateChanged(PinGroup.this, true);
                } else if (!isPinFilled() && isComplete) {
                    isComplete = false;
                    stateListener.onStateChanged(PinGroup.this, false);
                }
            }
            if (pinListener != null) {
                pinListener.onPinChanged(PinGroup.this, getText());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s) && focusedPin < getCount() - 1) {
                getChildAt(focusedPin + 1).requestFocus();
            } else if (TextUtils.isEmpty(s) && focusedPin > 0) {
                getChildAt(focusedPin - 1).requestFocus();
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
    private int textAppearance;
    private float textSize;
    private int textColor;
    private int focusedPin;
    private boolean isComplete;

    public PinGroup(@NonNull Context context) {
        this(context, null);
    }

    public PinGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.pinGroupStyle);
    }

    public PinGroup(
        @NonNull Context context,
        @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(PinUtils.getDrawable(context, R.attr.selectableItemBackground));
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setText("");
                return false;
            }
        });

        TypedArray a = context.obtainStyledAttributes(
            attrs,
            R.styleable.PinGroup,
            defStyleAttr,
            R.style.Widget_PinGroup
        );
        setCount(a.getInt(R.styleable.PinGroup_pinCount, DEFAULT_COUNT));
        setGap(a.getDimensionPixelSize(R.styleable.PinGroup_pinGap, 0));
        if (a.hasValue(R.styleable.PinGroup_android_text)) {
            setText(a.getText(R.styleable.PinGroup_android_text));
        }
        setTextAppearance(a.getResourceId(R.styleable.PinGroup_android_textAppearance, 0));
        if (a.hasValue(R.styleable.PinGroup_android_textSize)) {
            setTextSize(a.getDimension(R.styleable.PinGroup_android_textSize, 0f));
        }
        if (a.hasValue(R.styleable.PinGroup_android_textColor)) {
            setTextColor(a.getColor(R.styleable.PinGroup_android_textColor, 0));
        }
        a.recycle();
    }

    @Override
    public void setOrientation(int orientation) {
        Preconditions.checkArgument(
            orientation == LinearLayout.HORIZONTAL,
            "Vertical pins are not yet supported"
        );
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

    /**
     * Changes the number of pins expected, must be at least 1.
     *
     * @param count pin count.
     */
    public void setCount(int count) {
        Preconditions.checkArgument(count > 0, "Pin count must be at least 1");
        final int diff = count - getCount();
        if (diff < 0) {
            removeViews(count, -diff);
        } else if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                TextView view = new PinView(getContext());
                view.setOnFocusChangeListener(focusListener);
                view.addTextChangedListener(textWatcher);
                if (textAppearance != 0) {
                    PinUtils.setTextAppearance(view, textAppearance);
                }
                if (textSize != 0) {
                    view.setTextSize(textSize);
                }
                if (textColor != 0) {
                    view.setTextColor(textColor);
                }
                addView(view);
            }
        }
        textWatcher.onTextChanged(null, 0, 0, 0);
    }

    /**
     * Alias of {@link #getChildCount()}.
     *
     * @return the number of pin.
     */
    public int getCount() {
        return getChildCount();
    }

    /**
     * Changes the gap of each pins, will ignore negative number and pins must be at
     * least 2 to take effect.
     *
     * @param gap gap size in pixel.
     */
    public void setGap(@Px int gap) {
        pinGap = gap;
        applyGap();
    }

    /**
     * Returns the gap of each pins, regardless of whether or not it takes effect.
     *
     * @return gap of each pins.
     */
    @Px
    public int getGap() {
        return pinGap;
    }

    /**
     * Returns whether or not all pins are filled.
     *
     * @return true if all pins are filled
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Select pin with specified index.
     *
     * @param index of pin selected.
     */
    public void setSelection(int index) {
        final View selected = getChildAt(index);
        Preconditions.checkNotNull(selected, "Pin out of range");
        selected.requestFocus();
    }

    /**
     * Returns selected pin of this group.
     *
     * @return index of pin, or -1 if no pin are selected.
     */
    public int getSelection() {
        return map(new Function<TextView, Boolean>() {
            @Override
            public Boolean apply(TextView input) {
                return input.isFocused();
            }
        }, new Function<TextView, Integer>() {
            @Override
            public Integer apply(TextView input) {
                return indexOfChild(input);
            }
        }, -1);
    }

    /**
     * Change pins, must be all digits.
     *
     * @param text to change to.
     */
    public void setText(@NonNull CharSequence text) {
        final char[] pins = text.toString().toCharArray();
        forEachIndexed(new Consumer<Pair<TextView, Integer>>() {
            @Override
            public void accept(Pair<TextView, Integer> pair) {
                if (pair.second < pins.length) {
                    Preconditions.checkArgument(
                        Character.isDigit(pins[pair.second]),
                        "Text should be digits"
                    );
                    pair.first.setText(String.valueOf(pins[pair.second]));
                } else {
                    pair.first.setText("");
                }
            }
        });
    }

    /**
     * Change pins from resource, must be all digits.
     *
     * @param res text res to change to.
     */
    public void setText(@StringRes int res) {
        setText(getResources().getText(res));
    }

    /**
     * Returns current pin, never null.
     *
     * @return pins.
     */
    @NonNull
    public CharSequence getText() {
        final StringBuilder builder = new StringBuilder();
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView view) {
                builder.append(view.getText());
            }
        });
        return builder.toString();
    }

    /**
     * Change pin text appearance from resource.
     *
     * @param res text appearance to change to.
     */
    public void setTextAppearance(int res) {
        textAppearance = res;
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView view) {
                PinUtils.setTextAppearance(view, textAppearance);
            }
        });
    }

    /**
     * Returns current text appearance.
     *
     * @return text appearance.
     */
    public int getTextAppearance() {
        return textAppearance;
    }

    /**
     * Change pin text sizes.
     *
     * @param size of text to change to.
     */
    public void setTextSize(float size) {
        textSize = size;
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView view) {
                view.setTextSize(textSize);
            }
        });
    }

    /**
     * Returns current text size.
     *
     * @return text size.
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * Change pin text color.
     *
     * @param color of text to change to.
     */
    public void setTextColor(@ColorInt final int color) {
        textColor = color;
        forEach(new Consumer<TextView>() {
            @Override
            public void accept(TextView view) {
                view.setTextColor(color);
            }
        });
    }

    /**
     * Returns current text color.
     *
     * @return text color.
     */
    @ColorInt
    public int getTextColor() {
        return textColor;
    }

    /**
     * Register a listener that will be invoked when pins are changed.
     *
     * @param listener to register.
     */
    public void setOnPinChangedListener(@Nullable OnPinChangedListener listener) {
        pinListener = listener;
    }

    /**
     * Register a listener that will be invoked when state is changed.
     *
     * @param listener to register.
     */
    public void setOnStateChangedListener(@Nullable OnStateChangedListener listener) {
        stateListener = listener;
    }

    private boolean isPinFilled() {
        return map(new Function<TextView, Boolean>() {
            @Override
            public Boolean apply(TextView input) {
                return TextUtils.isEmpty(input.getText());
            }
        }, false, true);
    }

    private void applyGap() {
        if (pinGap > -1 && getCount() > 1) {
            forEach(new Consumer<TextView>() {
                @Override
                public void accept(TextView view) {
                    ((MarginLayoutParams) view.getLayoutParams()).setMargins(
                        pinGap / 2,
                        0,
                        pinGap / 2,
                        0
                    );
                }
            });
        }
    }

    private void forEach(Consumer<TextView> consumer) {
        for (int i = 0; i < getCount(); i++) {
            consumer.accept((TextView) getChildAt(i));
        }
    }

    private void forEachIndexed(Consumer<Pair<TextView, Integer>> consumer) {
        for (int i = 0; i < getCount(); i++) {
            consumer.accept(new Pair<>((TextView) getChildAt(i), i));
        }
    }

    private <V> V map(
        Function<TextView, Boolean> argument,
        Function<TextView, V> value,
        V defaultValue
    ) {
        for (int i = 0; i < getCount(); i++) {
            final TextView view = (TextView) getChildAt(i);
            if (argument.apply(view)) {
                return value.apply(view);
            }
        }
        return defaultValue;
    }

    private <V> V map(Function<TextView, Boolean> argument, final V value, V defaultValue) {
        return map(argument, new Function<TextView, V>() {
            @Override
            public V apply(TextView input) {
                return value;
            }
        }, defaultValue);
    }

    public interface OnPinChangedListener {

        /**
         * Called when pin is changed.
         *
         * @param view the view which pin has changed.
         * @param pin  changed pin in char sequence.
         */
        void onPinChanged(@NonNull PinGroup view, @NonNull CharSequence pin);
    }

    public interface OnStateChangedListener {

        /**
         * Called when state is changed.
         *
         * @param view       the view which state has changed.
         * @param isComplete true when pins are all filled.
         */
        void onStateChanged(@NonNull PinGroup view, boolean isComplete);
    }
}