PinView
=======
[![bintray](https://img.shields.io/badge/bintray-appcompat-brightgreen.svg)](https://bintray.com/hendraanggrian/appcompat)
[![download](https://api.bintray.com/packages/hendraanggrian/appcompat/pinview/images/download.svg) ](https://bintray.com/hendraanggrian/appcompat/pinview/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/pinview.svg)](https://travis-ci.com/hendraanggrian/pinview)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Android customizable PIN input view.

![demo][demo]

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

Download
--------
```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    compile 'com.hendraanggrian:pinedittext:0.1'
}
```

License
-------
    Copyright 2017 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[demo]: /art/demo.gif
