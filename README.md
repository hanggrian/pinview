PinView
=======
[![bintray](https://img.shields.io/badge/bintray-appcompat-brightgreen.svg)](https://bintray.com/hendraanggrian/appcompat)
[![download](https://api.bintray.com/packages/hendraanggrian/appcompat/pinview/images/download.svg) ](https://bintray.com/hendraanggrian/appcompat/pinview/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/pinview.svg)](https://travis-ci.com/hendraanggrian/pinview)
[![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Android customizable PIN input view.

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
<com.hendraanggrian.appcompat.widget.PinView
    android:id="@+id/pinView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:pinTextAppearance="@style/TextAppearance.AppCompat.Display2"
    app:pinCount="4" />
```

Then in java.

```java
PinView view = (PinView) findViewById(R.id.pinView);
CharSequence pin = view.getText();

// set listener
view.setOnStateChangedListener(new PinView.OnStateChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinEditText view, boolean isComplete) {
        // do something
    }
});
view.setOnPinChangedListener(new PinView.OnPinChangedListener() {
    @Override
    public void onStateChanged(@NonNull PinEditText view, @NonNull String... mPins) {
        // do something
    }
});
```

License
-------
    Copyright 2018 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[demo1]: /art/demo1.gif
[demo2]: /art/demo2.gif
