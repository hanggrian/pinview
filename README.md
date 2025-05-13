[![CircleCI](https://img.shields.io/circleci/build/gh/hanggrian/pinview)](https://app.circleci.com/pipelines/github/hanggrian/pinview/)
[![Codecov](https://img.shields.io/codecov/c/gh/hanggrian/pinview)](https://app.codecov.io/gh/hanggrian/pinview/)
[![Maven Central](https://img.shields.io/maven-central/v/com.hanggrian/pinview)](https://central.sonatype.com/artifact/com.hanggrian/pinview/)
[![Android SDK](https://img.shields.io/badge/sdk-21%2B-informational)](https://developer.android.com/studio/releases/platforms/#5.0) \
[![Figma](https://img.shields.io/badge/design-figma-f24e1e)](https://www.figma.com/community/file/1502826401449912665/)
[![Layers](https://img.shields.io/badge/showcase-layers-000)](https://layers.to/layers/cmamk4jbf000gl70cqz0z2uc2/)
[![Pinterest](https://img.shields.io/badge/pin-pinterest-bd081c)](https://www.pinterest.com/pin/1107322627133856191/)

# PinView

![](https://github.com/hendraanggrian/pinview/raw/assets/preview_shown.png "Digits-shown preview")
![](https://github.com/hendraanggrian/pinview/raw/assets/preview_hidden.png "Digits-hidden preview")

Customizable pin input field with a jumping cursor.

- Inflate custom `EditText` from XML.
- Tracks user input and state changes.

## Download

```gradle
repositories {
    google()
    mavenCentral()
}
dependencies {
    compile "com.hanggrian:pinview:$version"
}
```

## Usage

Declare view in xml layout.

```xml
<com.hanggrian.pinview.PinGroup
  android:id="@+id/pinGroup"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"
  app:pinCount="6"/>
```

Then in Java.

```java
PinGroup view = findViewById<>(R.id.pinView);
CharSequence pin = view.getText();

view.setOnStateChangedListener((view, isComplete) -> {
    // ...
});
view.setOnPinChangedListener((view, pin) -> {
    // ...
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
<com.hanggrian.pinview.PinGroup
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  app:pinView="com.example.CustomPinView"/>
```
