package com.example.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainScreen extends AppCompatActivity {

    TextView quizTxt, option1, option2, option3, option4;
    TextView current, max, timeCounter;
    String ANSWER;

    List list;
    int numQues, Counter = 0;

    LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        quizTxt = findViewById(R.id.quizTxt);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        timeCounter = findViewById(R.id.time_counter);

        progressIndicator = findViewById(R.id.progress);

        current = findViewById(R.id.current);
        max = findViewById(R.id.max);

        list = new ArrayList<>();
        String response = getIntent().getStringExtra("response");
        numQues = Integer.parseInt(getIntent().getStringExtra("numQues"));

        progressIndicator.setMax(numQues);
        max.setText(String.valueOf(numQues));
        current.setText("0");

        DataAdder(response);
        SetScreen((Data) list.get(Counter));
        ButtonSetter();
    }

    public void DataAdder(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println(jsonObject);
            for (int i = 0; i < numQues; i++) {
                String question = jsonObject.getJSONArray("results").getJSONObject(i).getString("question");
                System.out.println(question);
                question = question.replace("&#039;", "'");
                question = question.replace("&quot;", "\"");
                String answer = jsonObject.getJSONArray("results").getJSONObject(i).getString("correct_answer");
                answer = answer.replace("&#039;", "'");
                answer = answer.replace("&quot;", "\"");
                String[] option = JsonToArray(jsonObject.getJSONArray("results").getJSONObject(i).getJSONArray("incorrect_answers"));
                option[3] = answer;
                shuffleArray(option);
                list.add(new Data(question, option, answer));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] JsonToArray(JSONArray jsonArray) {
        String[] listdata = new String[4];

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    String temp = jsonArray.getString(i);
                    temp = temp.replace("&#039;", "'");
                    temp = temp.replace("&quot;", "\"");
                    listdata[i] = temp;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return listdata;
    }

    public void ButtonSetter() {
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setter(option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setter(option2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setter(option3);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setter(option4);
            }
        });
    }

    public void setter(TextView view) {
        if (view.getText().toString().equals(ANSWER)) {
            Toast.makeText(MainScreen.this, "correct", Toast.LENGTH_SHORT).show();
            progressIndicator.setProgress(++Counter);
            current.setText(String.valueOf(Counter));
//            if (Counter <= 9) {
//                SetScreen((Data) list.get(Counter));
//            } else {
//                ResultIntentCall();
//            }
            CorrectTimeCounter(view);
        } else {
//            ResultIntentCall();
            WrongTimerCounter(view);
        }

    }

    public void ResultIntentCall() {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        Pair[] pair = new Pair[1];
        pair[0] = new Pair<View, String>(progressIndicator, "progress_bar_transition");
        intent.putExtra("result", String.valueOf(Counter));
        intent.putExtra("numQues", String.valueOf(numQues));
        ActivityOptions activityOptions =ActivityOptions.makeSceneTransitionAnimation(MainScreen.this, pair);
        startActivity(intent, activityOptions.toBundle());
        finish();
    }

    public String[] shuffleArray(String[] array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        System.out.println(Arrays.toString(array));
        Random rnd = new Random();
        for (int i = array.length - 1; i >= 0; i--) {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            String object = array[j];
            array[j] = array[i];
            array[i] = object;
        }
        System.out.println(Arrays.toString(array));
        return array;
    }

    public void SetScreen(Data data) {
        String question = data.question;
        String[] options = data.option;
        this.ANSWER = data.answer;
        quizTxt.setText(question);
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
    }

    public void CorrectTimeCounter(TextView view) {
        view.setBackgroundResource(R.drawable.correct_boder);
        view.setPadding(10, 30, 10, 30);
        timeCounter.setVisibility(View.VISIBLE);
        Disable();
        new CountDownTimer(5000, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                view.setBackgroundResource(R.drawable.boder);
                Enable();
                if (Counter <= 9) {
                    SetScreen((Data) list.get(Counter));
                    view.setPadding(10, 30, 10, 30);
                    timeCounter.setVisibility(View.INVISIBLE);
                } else {
                    ResultIntentCall();
                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
                timeCounter.setText(String.valueOf(millisUntilFinished/1000));
            }
        }.start();
    }

    public void WrongTimerCounter(TextView view) {
        view.setBackgroundResource(R.drawable.wrong_boder);
        view.setPadding(10, 30, 10, 30);
        timeCounter.setVisibility(View.VISIBLE);
        Disable();
        new CountDownTimer(5000, 1000) {
            public void onFinish() {
                SetScreen((Data) list.get(Counter));
                Enable();
                view.setPadding(10, 30, 10, 30);
                timeCounter.setVisibility(View.INVISIBLE);
                ResultIntentCall();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
                timeCounter.setText(String.valueOf(millisUntilFinished/1000));
            }
        }.start();
    }

    public void Enable(){
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    public void Disable(){
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }
}