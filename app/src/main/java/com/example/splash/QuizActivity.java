package com.example.splash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    ArrayList<Question> questionArrayList;
    RadioGroup rb_answerOptions;
    Button btn_Quit;
    Button btn_Next;
    ImageView iv_QuestionImage;
    TextView tv_QuestionId, tv_QuestionText;
    int totalScore = 0;
    int questionId = 0;

    ProgressDialog progressDialog;
    int progressIncr = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quiz");

        rb_answerOptions = (RadioGroup) findViewById(R.id.rb_AnswerOptions);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        btn_Quit = (Button) findViewById(R.id.btn_Quit);
        iv_QuestionImage = (ImageView) findViewById(R.id.iv_QuestionImage);
        tv_QuestionId = (TextView) findViewById(R.id.tv_QuestionNumber);
        tv_QuestionText = (TextView) findViewById(R.id.tv_QuestionText);

        if (getIntent() != null) {
            questionArrayList = getIntent().getParcelableArrayListExtra("QUESTION_LIST"); //new ArrayList<>();
            Log.d("QuestionList Length: ", "" + questionArrayList.size());
        }

        for (int i = 0; i < questionArrayList.size(); i++) {
            Log.d("QUESTIONS: ", questionArrayList.get(i).getQuestionText().toString());
        }
        //  MyParcelable object = (MyParcelable) getIntent().getParcelableExtra("myData");
        if (isConnectionOnline()) {
            SetUpQuestions(questionId);
            questionId++;
        } else
            Toast.makeText(QuizActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();


//Onclick Listner for Next Button
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb_answerOptions.getCheckedRadioButtonId() < 0) {
                    Toast.makeText(QuizActivity.this, "Please Select an Answer", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Question Number", ": " + questionId);
                    Log.d("CheckedRadioButtonId", ": " + rb_answerOptions.getCheckedRadioButtonId());
                    String answer = ((RadioButton) findViewById(rb_answerOptions.getCheckedRadioButtonId())).getText().toString();
                    Log.d("Answer: ", answer);

                    int questionId1 = Integer.parseInt(tv_QuestionId.getText().toString().substring(1)) - 1;
                    Log.d("Answer Value:", " " + questionArrayList.get(questionId1).getAnswerOptions().get(answer));

                    if (questionId < questionArrayList.size()) {
                        totalScore = totalScore + questionArrayList.get(questionId1).getAnswerOptions().get(answer);
                        Log.d("Total Score: ", "" + totalScore);
                        rb_answerOptions.removeAllViews();
                        SetUpQuestions(questionId);
                        questionId++;
                    } else {
                        Intent i = new Intent(QuizActivity.this, ResultActivity.class);
                        i.putParcelableArrayListExtra("QUESTION_LIST", questionArrayList);
                        i.putExtra("TOTAL_SCORE", totalScore);
                        startActivity(i);
                        finish();
                    }
                    rb_answerOptions.clearCheck();
                    Log.d("Btn ID After clearcheck", ":" + rb_answerOptions.getCheckedRadioButtonId());
                }
            }
        });

        btn_Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }//end of OnCreate

    public void SetUpQuestions(int questionId) {
        int questionNumber = questionArrayList.get(questionId).getQuestionID() + 1;
        tv_QuestionId.setText("Q" + questionNumber);
        if (null != questionArrayList.get(questionId).getQuestionImage() && !questionArrayList.get(questionId).getQuestionImage().trim().equalsIgnoreCase("")) {
            progressDialog = new ProgressDialog(QuizActivity.this);
            GetImage getImage = new GetImage();
            getImage.execute(questionArrayList.get(questionId).getQuestionImage());
        } else {
            Log.d("Image Link:", "Image Link" + questionArrayList.get(questionId).getQuestionImage());
            iv_QuestionImage.setImageDrawable(null);
        }


        tv_QuestionText.setText(questionArrayList.get(questionId).getQuestionText());
        List<String> answerOptions = new ArrayList<String>(questionArrayList.get(questionId).getAnswerOptions().keySet());
        Collections.shuffle(answerOptions);
        int i = 0;
        for (String answer : answerOptions) {
            RadioButton rb_Choice = new RadioButton(this);
            rb_Choice.setText(answer);
            rb_Choice.setId(i);
            rb_answerOptions.addView(rb_Choice);
            i++;
        }
        // i=-1;
    }


    private class GetImage extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                Bitmap image = BitmapFactory.decodeStream(con.getInputStream());

                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressDialog.dismiss();
            iv_QuestionImage.setImageBitmap(bitmap);

        }

        @Override
        protected void onPreExecute() {
            CharSequence message = "Loading Image";
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.incrementProgressBy(progressIncr);
        }
    }

    private boolean isConnectionOnline() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conManager.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            return false;
    }

}
