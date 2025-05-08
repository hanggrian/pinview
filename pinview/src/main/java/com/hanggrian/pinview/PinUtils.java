package com.hanggrian.pinview;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

final class PinUtils {
    private static final ThreadLocal<Map<String, Constructor<PinEditText>>> CONSTRUCTORS =
        new ThreadLocal<>();
    private static final Class<?>[] CONSTRUCTOR_PARAMS = new Class<?>[]{Context.class};

    private PinUtils() {}

    static void setTextAppearance(TextView view, int res) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setTextAppearance(res);
            return;
        }
        TextViewCompat.setTextAppearance(view, res);
    }

    /**
     * Stolen from {@code CoordinatorLayout.parseBehavior(Context, AttributeSet, String)}.
     */
    static PinEditText parsePinEditText(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        final String fullName;
        if (name.startsWith(".")) {
            // relative to the app package. Prepend the app package name
            fullName = context.getPackageName() + name;
        } else if (name.indexOf('.') >= 0) {
            // fully qualified package name
            fullName = name;
        } else {
            // assume stock behavior in this package
            fullName = "com.hanggrian.pinview" + name;
        }
        try {
            Map<String, Constructor<PinEditText>> constructors = CONSTRUCTORS.get();
            if (constructors == null) {
                constructors = new HashMap<>();
                CONSTRUCTORS.set(constructors);
            }
            Constructor<PinEditText> c = constructors.get(fullName);
            if (c == null) {
                final Class<PinEditText> clazz =
                    (Class<PinEditText>) Class.forName(fullName, true, context.getClassLoader());
                c = clazz.getConstructor(CONSTRUCTOR_PARAMS);
                constructors.put(fullName, c);
            }
            return c.newInstance(context);
        } catch (
            ClassNotFoundException
            | NoSuchMethodException
            | IllegalAccessException
            | InstantiationException
            | InvocationTargetException e
        ) {
            throw new RuntimeException("Could not inflate Behavior subclass " + fullName, e);
        }
    }
}
