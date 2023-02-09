package com.example.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class QuizChooser extends AppCompatActivity {

    AutoCompleteTextView numberQues, category, difficulty;
    String num = "amount=",cate = "&category=", diff= "&difficulty=", defaultNum = "10";
    Map<String, Integer> map;

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_chooser);

        numberQues = findViewById(R.id.number_of_ques);
        category = findViewById(R.id.category);
        difficulty = findViewById(R.id.difficulty);
        loading = findViewById(R.id.loading);

        String[] numberOfQues = {"1","2","3","4","5","6","7","8","9","10"};
        ArrayAdapter adapter1 = new ArrayAdapter<>(this, R.layout.auto_complete_textview_apapter_view, numberOfQues);
        numberQues.setAdapter(adapter1);

        String[] categoryList = {"General Knowledge", "Entertainment: Books", "Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals & Theatres", "Entertainment: Television", "Entertainment: Video Games", "Entertainment: Board Games", "Science & Nature", "Science: Computers", "Science: Mathematics", "Mythology", "Sports", "Geography", "History", "Vehicles", "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime &; Manga", "Entertainment: Cartoon & Animations"};
        ArrayAdapter adapter2 = new ArrayAdapter<>(this, R.layout.auto_complete_textview_apapter_view, categoryList);
        category.setAdapter(adapter2);

        map = new HashMap<String, Integer>();
        for (int i = 9; i < categoryList.length+9; i++) {
            if (i != 24 && i != 25 && i != 26 && i != 27) {
                map.put(categoryList[i - 9], i);
            }
        }

        String[] difficultList = {"Easy", "Medium", "hard"};
        ArrayAdapter adapter3 = new ArrayAdapter<>(this, R.layout.auto_complete_textview_apapter_view, difficultList);
        difficulty.setAdapter(adapter3);

        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading.setVisibility(view.VISIBLE);


                RequestQueue requestQueue;
                StringRequest stringRequest;
                String url = Url();
                requestQueue = Volley.newRequestQueue(getApplicationContext());

                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                            intent.putExtra("response", response);
                            intent.putExtra("numQues", defaultNum);
                            loading.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(QuizChooser.this, "connection error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QuizChooser.this, "connection error", Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(stringRequest);
            }
        });
    }

    public String Url(){

        String url = "https://opentdb.com/api.php?";

        if (!numberQues.getText().toString().equals("")){
            defaultNum = numberQues.getText().toString();
            url = url.concat(num+defaultNum);
        }else {
            url = url.concat(num+defaultNum);
        }

        if (!category.getText().toString().equals("")){
            String mapVal = String.valueOf(map.get(category.getText().toString()));
            url = url.concat(cate+mapVal);
        }

        if (!difficulty.getText().toString().equals("")){
            url = url.concat(diff+difficulty.getText().toString().toLowerCase());
        }

        url = url.concat("&type=multiple");

        return url;
    }
}