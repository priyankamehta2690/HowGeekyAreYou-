package com.example.splash;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class WelcomeActivity extends AppCompatActivity {

    Button btn_StartQuiz, btn_ExitQuiz;
    ProgressDialog retrieveProgress;
    int prgsIncrement=1;
    int questionCounter =0;
    ArrayList<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle("Welcome");

        btn_StartQuiz = (Button) findViewById(R.id.btn_StartQuiz);
        btn_ExitQuiz = (Button) findViewById(R.id.btn_ExitQuiz);
        btn_StartQuiz.setEnabled(false);
        questionList=new ArrayList<>();

        if(isConnectionOnline()){
            retrieveProgress = new ProgressDialog(WelcomeActivity.this);
            for(int i=0;i<7; i++){
                GetQuizQuestions getQuestions = new GetQuizQuestions();
                getQuestions.execute("http://dev.theappsdr.com/apis/spring_2016/hw3/index.php?qid="+i);
            }

            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
        }

        //Start Quiz onClickListner
        btn_StartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // MyParcelable dataToSend = new MyParcelable();
                Log.d("QuestionList Size(Wel)", "" + questionList.size());
                Intent i = new Intent(WelcomeActivity.this, QuizActivity.class);
                // i.putExtra("myData", dataToSend); // using the (String name, Parcelable value) overload!
                i.putParcelableArrayListExtra("QUESTION_LIST", questionList);
                startActivity(i);

            }
        });

        //Exit button onClick Listner
        btn_ExitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }//end of OnCreate

  private class GetQuizQuestions extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url= new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";

                while((line=reader.readLine())!=null)
                {
                    //System.out.println("line = "+line);
                    String[] data = line.split(";");
                    Question currentQuestion = new Question();
                    HashMap<String,Integer> answerOptions = new HashMap<>();
                    currentQuestion.setQuestionID(Integer.parseInt(data[0]));
                    currentQuestion.setQuestionText(data[1]);
                    int indexPosition = 2;
                    while (indexPosition < data.length)
                    {
                        if (data[indexPosition].startsWith("http"))
                        {
                            // It is the question image.
                        //    System.out.println("Link=" + data[indexPosition]);
                            currentQuestion.setQuestionImage(data[indexPosition]);

                        }
                        else
                        {
                            //It is either answer or the score of the answer.
                           // System.out.println("Option=" + data[indexPosition] + " Score=" + data[indexPosition + 1]);
                            answerOptions.put(data[indexPosition], Integer.parseInt(data[indexPosition + 1]));
                            indexPosition++;
                        }
                        indexPosition++;
                    }
                    currentQuestion.setAnswerOptions(answerOptions);
                    questionList.add(currentQuestion);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("questionList="+questionList);
            super.onPostExecute(s);
            retrieveProgress.dismiss();
            btn_StartQuiz.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrieveProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            retrieveProgress.setCancelable(false);
            retrieveProgress.setMessage("Downloading Questions");
            retrieveProgress.setProgress(0);
            retrieveProgress.setMax(100);
            retrieveProgress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            retrieveProgress.incrementProgressBy(prgsIncrement);
        }

    }//end of GetQuestions

private boolean isConnectionOnline(){
    ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = conManager.getActiveNetworkInfo();
    if(info!=null && info.isConnected())
        return true;
    else
        return false;
}

}
