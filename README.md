[![Travis CI](https://img.shields.io/travis/com/hendraanggrian/appcompat/pinview)](https://travis-ci.com/github/hendraanggrian/pinview/)
[![Codecov](https://img.shields.io/codecov/c/github/hendraanggrian/pinview)](https://codecov.io/gh/hendraanggrian/pinview/)
[![Maven Central](https://img.shields.io/maven-central/v/com.hendraanggrian.appcompat/pinview)](https://repo1.maven.org/maven2/com/hendraanggrian/appcompat/pinview/)
[![Nexus Snapshot](https://img.shields.io/nexus/s/com.hendraanggrian.appcompat/pinview?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/hendraanggrian/appcompat/pinview/)
[![Android SDK](https://img.shields.io/badge/sdk-14%2B-informational)](https://developer.android.com/studio/releases/platforms/#4.0)

# PinView

Android customizable pin input view.

![Preview.](https://github.com/hendraanggrian/pinview/raw/assets/preview.png)

## Download

```gradle
repositories {
    google()
    mavenCentral()
}
dependencies {
    compile "com.hendraanggrian.appcompat:pinview:$version"
}
```

## Usage

Declare view in xml layout.

```xml
<com.hendraanggrian.appcompat.widget.PinGroup
  android:id="@+id/pinGroup"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"
  app:pinCount="6"/>
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

### Use custom pin

Make a class that extends `PinView`.

```java
package com.example;

public class CustomPinView extends PinView {
  public CustomPinView(Context context) {
    super(context);
    doSomething();
  }
}
```

Then refer to that class in xml, there is no way to do change it
programmatically.

```xml
<com.hendraanggrian.appcompat.widget.PinGroup
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  app:pinView="com.example.CustomPinView"/>
```
