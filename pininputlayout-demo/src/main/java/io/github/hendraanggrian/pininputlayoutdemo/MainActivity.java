package io.github.hendraanggrian.pininputlayoutdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.github.hendraanggrian.pininputlayout.PinInputLayout;

public class MainActivity extends AppCompatActivity {

    private PinInputLayout pinInputLayout;
    private TextView textViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewState = (TextView) findViewById(R.id.textview_state);

        pinInputLayout = (PinInputLayout) findViewById(R.id.pininputlayout);
        pinInputLayout.setOnStateChangedListener(new PinInputLayout.OnStateChangedListener() {
            @Override
            public void onStateChanged(@NonNull PinInputLayout view, @NonNull PinInputLayout.State state) {
                textViewState.setText(state.toString());
            }
        });
    }
}