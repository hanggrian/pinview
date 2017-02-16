![logo](/art/logo.png) PinInputLayout
=====================================
Android customizable PIN input view.

![demo](/art/demo.gif)

Download
--------
```gradle
compile 'io.github.hendraanggrian:pininputlayout:0.1.4'
```

Usage
-----
Declare view in xml layout.
```xml
<io.github.hendraanggrian.pininputlayout.PinInputLayout
        android:id="@+id/pininputlayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pinDigits="4"
        app:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"/>
```

Then in java.
```java
PinInputLayout pinInputLayout = (PinInputLayout) findViewById(R.id.pininputlayout);
pinInputLayout.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
String code = pinInputLayout.getText();

// set listener
pinInputLayout.setOnStateChangedListener(new PinInputLayout.OnStateChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinInputLayout view, @NonNull PinInputLayout.State state) {
        // do something
    }
});
pinInputLayout.setOnPinChangedListener(new PinInputLayout.OnPinChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinInputLayout view, @NonNull String... pins) {
        // do something
    }
});
```