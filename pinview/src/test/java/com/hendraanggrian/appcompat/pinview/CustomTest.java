package com.hendraanggrian.appcompat.pinview;

import static org.junit.Assert.assertEquals;

import androidx.appcompat.app.AppCompatActivity;
import com.hendraanggrian.appcompat.pinview.test.R;
import com.hendraanggrian.appcompat.pinview.widget.PinView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.internal.DoNotInstrument;

@RunWith(RobolectricTestRunner.class)
@DoNotInstrument
public class CustomTest {
  private AppCompatActivity activity;
  private PinView pinView;

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(CustomActivity.class).setup().get();
    pinView = (PinView) activity.getLayoutInflater().inflate(R.layout.activity_test, null);
  }

  @Test
  public void changedProperties() {
    assertEquals(0, pinView.getGap());
  }
}
