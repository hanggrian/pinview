package com.hanggrian.pinview;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.hanggrian.pinview.test.R;

public class DefaultActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
  }
}
