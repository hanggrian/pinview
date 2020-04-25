[![download](https://api.bintray.com/packages/hendraanggrian/appcompat/pinview/images/download.svg) ](https://bintray.com/hendraanggrian/appcompat/pinview/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/pinview.svg)](https://travis-ci.com/hendraanggrian/pinview)
[![license](https://img.shields.io/github/license/hendraanggrian/pinview)](http://www.apache.org/licenses/LICENSE-2.0)

PinView
=======
Android customizable pin input view.

![demo1][demo1]
![demo2][demo2]

Download
--------
```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    compile "com.hendraanggrian.appcompat:pinview:$version"
}
```

Usage
-----
Declare view in xml layout.

```xml
<com.hendraanggrian.appcompat.widget.PinGroup
    android:id="@+id/pinGroup"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"
    app:pinCount="6" />
```

Then in java.

```java
PinGroup view = findViewById<>(R.id.pinView);
CharSequence pin = view.getText();

// set listener
view.setOnStateChangedListener(new PinGroup.OnStateChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinGroup view, boolean isComplete) {
        // do something
    }
});
view.setOnPinChangedListener(new PinGroup.OnPinChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinGroup view, @NonNull CharSequence pin) {
        // do something
    }
});
```

#### Use custom pin

Make a class that extends `PinView`.

```java
package com.example;

public class CustomPinView extends com.hendraanggrian.appcompat.widget.PinView {
    
    public CustomPinView(Context context) {
        super(context);
        doSomething();
    }
}
```

Then refer to that class in xml, there is no way to do change it programmatically.

```xml
<com.hendraanggrian.appcompat.widget.PinGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:pinView="com.example.CustomPinView" />
```

[demo1]: /art/demo1.gif
[demo2]: /art/demo2.gif
