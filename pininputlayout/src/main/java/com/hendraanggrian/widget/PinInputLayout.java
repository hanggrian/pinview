package com.hendraanggrian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hendraanggrian.widget.pininputlayout.PinInput;
import com.hendraanggrian.widget.pininputlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class PinInputLayout extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {

    private static final int DEFAULT_PIN_DIGITS = 4;

    @NonNull private final List<PinInput> pinInputs;
    private int focusedPin;
    private State currentState = State.INCOMPLETE;
    @Nullable private OnStateChangedListener onStateChangedListener;
    @Nullable private OnPinChangedListener onPinChangedListener;

    public PinInputLayout(Context context) {
        this(context, null);
    }

    public PinInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PinInputLayout, defStyleAttr, 0);
        try {
            int pinDigits = array.getInt(R.styleable.PinInputLayout_pinDigits, DEFAULT_PIN_DIGITS);
            int pinMargin = (int) array.getDimension(R.styleable.PinInputLayout_pinMargin, context.getResources().getDimension(R.dimen.margin_pin));
            int pinBackground = array.getResourceId(R.styleable.PinInputLayout_pinBackground, -1);
            int pinTextAppearance = array.getResourceId(R.styleable.PinInputLayout_pinTextAppearance, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                    ? android.R.style.TextAppearance_Material_Display1
                    : android.R.style.TextAppearance_Large);
            int pinTextColor = array.getColor(R.styleable.PinInputLayout_pinTextColor, -1);
            float pinTextSize = array.getDimension(R.styleable.PinInputLayout_pinTextSize, -1);

            pinInputs = new ArrayList<>();
            for (int i = 0; i < pinDigits; i++) {
                pinInputs.add(new PinInput(context, this));
                pinInputs.get(i).setMargin(i > 0 ? pinMargin : 0, 0, 0, 0);
                pinInputs.get(i).setTextAppearance(context, pinTextAppearance);
                if (pinBackground != -1)
                    pinInputs.get(i).setBackgroundResource(pinBackground);
                if (pinTextColor != -1)
                    pinInputs.get(i).setTextColor(pinTextColor);
                if (pinTextSize != -1)
                    pinInputs.get(i).setTextSize(pinTextSize);
                addView(pinInputs.get(i));
            }
        } finally {
            array.recycle();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (onStateChangedListener != null) {
            if (arePinsFilled() && currentState != State.COMPLETE) {
                currentState = State.COMPLETE;
                onStateChangedListener.onStateChanged(this, State.COMPLETE);
            } else if (!arePinsFilled() && currentState != State.INCOMPLETE) {
                currentState = State.INCOMPLETE;
                onStateChangedListener.onStateChanged(this, State.INCOMPLETE);
            }
        }
        if (onPinChangedListener != null) {
            String[] pins = new String[pinInputs.size()];
            for (int i = 0; i < pinInputs.size(); i++)
                pins[i] = pinInputs.get(i).getText().toString();
            onPinChangedListener.onPinChanged(this, pins);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().isEmpty() && focusedPin < pinInputs.size() - 1)
            pinInputs.get(focusedPin + 1).focus();
        else if (s.toString().isEmpty() && focusedPin > 0)
            pinInputs.get(focusedPin - 1).focus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            focusedPin = pinInputs.indexOf((PinInput) v);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        for (PinInput pinInput : pinInputs)
            pinInput.addTextChangedListener(textWatcher);
    }

    public void setOnStateChangedListener(@Nullable OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    @Nullable
    public OnStateChangedListener getOnStateChangedListener() {
        return onStateChangedListener;
    }

    public void setOnPinChangedListener(@Nullable OnPinChangedListener onPinChangedListener) {
        this.onPinChangedListener = onPinChangedListener;
    }

    @Nullable
    public OnPinChangedListener getOnPinChangedListener() {
        return onPinChangedListener;
    }

    public String getText() {
        String code = "";
        for (PinInput PinInput : pinInputs)
            code += PinInput.getText();
        return code;
    }

    public void clear() {
        for (PinInput pinInput : pinInputs)
            pinInput.setText("");
    }

    public void setInputType(int type) {
        for (PinInput pinInput : pinInputs)
            pinInput.setInputType(type);
    }

    private boolean arePinsFilled() {
        for (PinInput pinInput : pinInputs)
            if (pinInput.getText().toString().isEmpty())
                return false;
        return true;
    }

    public enum State {
        INCOMPLETE, COMPLETE
    }

    public interface OnStateChangedListener {
        void onStateChanged(@NonNull PinInputLayout view, @NonNull State state);
    }

    public interface OnPinChangedListener {
        void onPinChanged(@NonNull PinInputLayout view, @NonNull String... pins);
    }
}