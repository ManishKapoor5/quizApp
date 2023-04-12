package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;

    private List<Item> mList;
    private TextView questionTV, questionNumberTV;
    private Button option1Btn, option2Btn, option3Btn, option4Btn;
    private ArrayList<QuizModal> quizModalArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 1, currentPos;



    ViewFlipper v_flipper;
    private ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int images[] = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
        v_flipper = findViewById(R.id.v_flipper);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        mList = new ArrayList<>();
        fetchData();
        //for loop
        for(int i=0;i <images.length;i++){
            flipperImage(images[i]);
        }


        questionTV = findViewById(R.id.idTVQuestion);
        questionNumberTV = findViewById(R.id.idTVQuestionAttempted);
        option1Btn = findViewById(R.id.idBtnOption1);
        option2Btn = findViewById(R.id.idBtnOption2);
        option3Btn = findViewById(R.id.idBtnOption3);
        option4Btn = findViewById(R.id.idBtnOption4);
        quizModalArrayList = new ArrayList<>();
        random = new Random();
        getQuizQuestion(quizModalArrayList);
        currentPos = random.nextInt(quizModalArrayList.size());
        setDataToViews(currentPos);
        option1Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase(Locale.ROOT).equals(option1Btn.getText().toString().toLowerCase(Locale.ROOT).trim())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option2Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase(Locale.ROOT).equals(option2Btn.getText().toString().toLowerCase(Locale.ROOT).trim())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option3Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase(Locale.ROOT).equals(option3Btn.getText().toString().toLowerCase(Locale.ROOT).trim())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option4Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase(Locale.ROOT).equals(option4Btn.getText().toString().toLowerCase(Locale.ROOT).trim())) {
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);

            }
        });
    }

    private void fetchData() {
        String url ="https://dummyjson.com/users/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("users");
                    for(int i=0; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String imageUrl = jsonObject.getString("image");

                        Item post = new Item(imageUrl);
                        mList.add(post);
                    }

                    PostAdapter adapter = new PostAdapter(MainActivity.this,mList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_bottom_sheet, (LinearLayout) findViewById(R.id.idLLScore));
        TextView scoreTV = bottomSheetView.findViewById((R.id.idTVScore));
        Button restartQuizBtn = bottomSheetView.findViewById(R.id.idBtnRestart);
        scoreTV.setText("Your  score is :\n" + currentScore + "/10");
        restartQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
                questionAttempted = 1;
                currentScore = 0;
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setDataToViews(int currentPos) {
        questionNumberTV.setText("Question Attempted :" + questionAttempted + "/10");
        if (questionAttempted == 10) {
            showBottomSheet();
        } else {
            questionTV.setText(quizModalArrayList.get(currentPos).getQuestion());
            option1Btn.setText(quizModalArrayList.get(currentPos).getOption1());
            option2Btn.setText(quizModalArrayList.get(currentPos).getOption2());
            option3Btn.setText(quizModalArrayList.get(currentPos).getOption3());
            option4Btn.setText(quizModalArrayList.get(currentPos).getOption4());
        }

    }

    private void getQuizQuestion(ArrayList<QuizModal> quizModalArrayList) {
        quizModalArrayList.add(new QuizModal("Who found India first ?", "Alexander Graham bell", "Isaac Newton", "Vasco da Gama", "Nikola Tesla", "Vasco da Gama"));
        quizModalArrayList.add(new QuizModal("What country has the highest life expectancy? ", "Japan", "Russia", "Australia", "Hong Kong", "Hong Kong"));
        quizModalArrayList.add(new QuizModal("Where would you be if you were standing on the Spanish Steps?", "Rome", "America", "Italy", "Croatia", "Rome"));
        quizModalArrayList.add(new QuizModal("Which language has the more native speakers", "English", "Spanish", "Japanese", "Hindi", "Spanish"));
        quizModalArrayList.add(new QuizModal("What is the most common surname in the United States?", "Warner", "Johnson", "Downey", "Smith", "Smith"));
        quizModalArrayList.add(new QuizModal("Who was the Ancient Greek God of the Sun?", "Apollo", "Surya", "Thor", "Hercules", "Vasco da Gama"));
        quizModalArrayList.add(new QuizModal("What year was the United Nations established", "1940", "1930", "1810", "1945", "1945"));
        quizModalArrayList.add(new QuizModal("Who has won the most total Academy Awards?", "Warner Bros", "20th Century", "Walt Disney", "Marvels", "Walt Disney"));
        quizModalArrayList.add(new QuizModal("How many minutes are in a full week?", "10080", "11003", "15000", "7500", "10080"));
        quizModalArrayList.add(new QuizModal("What country drinks the most coffee per capita?", "New Zealand", "Finland", "America", "South Africa", "Finland"));

    }

    public void flipperImage(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(3000);
        v_flipper.setAutoStart(true);
    }

}

