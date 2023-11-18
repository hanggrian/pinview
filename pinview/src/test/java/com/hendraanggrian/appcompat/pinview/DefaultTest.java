package com.hendraanggrian.appcompat.pinview;

import static org.junit.Assert.assertTrue;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import com.hendraanggrian.appcompat.pinview.test.R;
import com.hendraanggrian.appcompat.pinview.widget.PinView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@DoNotInstrument
public class DefaultTest {
  private AppCompatActivity activity;
  private PinView pinView;

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(DefaultActivity.class).setup().get();
    pinView = (PinView) activity.getLayoutInflater().inflate(R.layout.activity_test, null);
  }

  @Test
  public void defaultProperties() {
    assertTrue(pinView.getText().toString().isEmpty());
  }
}
