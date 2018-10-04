package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

class PinUtils {

    private PinUtils() {
    }

    static void setTextAppearance(TextView view, int res) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setTextAppearance(res);
        } else {
            TextViewCompat.setTextAppearance(view, res);
        }
    }

    static int getDrawable(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }
}
