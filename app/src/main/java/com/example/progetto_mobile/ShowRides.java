package com.example.progetto_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowRides extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.showrides_layout);
        layout = findViewById(R.id.linear);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        for (int i = 0; i < 3; i++) {
            TextView dynamicTextView = new TextView(this);

            dynamicTextView.setLayoutParams(params);
            dynamicTextView.setText("NewYork");
            layout.addView(dynamicTextView);

        }
    }
}
