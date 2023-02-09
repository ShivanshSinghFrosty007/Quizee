package com.example.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ResultActivity extends AppCompatActivity {

    TextView result;
    LinearProgressIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);
        indicator = findViewById(R.id.progress);

        String res = getIntent().getStringExtra("result");
        int numQues = Integer.parseInt(getIntent().getStringExtra("numQues"));

        indicator.setMax(numQues);
        indicator.setProgress(Integer.parseInt(res.toString()));

        result.setText(res);

    }
}