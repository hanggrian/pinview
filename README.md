PinInputLayout
==============
Android customizable PIN input view.

![demo](/art/demo.gif)

Download
--------
```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    compile 'com.hendraanggrian:pinedittext:0.1.0'
}
```

Usage
-----
Declare view in xml layout.
```xml
<com.hendraanggrian.PinEditText
    android:id="@+id/editText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"
    app:pinCount="4" />
```

Then in java.
```java
PinEditText editText = (PinEditText) findViewById(R.id.editText);
CharSequence pin = editText.getText();

// set listener
editText.setOnStateChangedListener(new OnStateChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinEditText view, boolean isComplete) {
        // do something
    }
});
editText.setOnPinChangedListener(new OnPinChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinEditText view, @NonNull String... mPins) {
        // do something
    }
});
```
