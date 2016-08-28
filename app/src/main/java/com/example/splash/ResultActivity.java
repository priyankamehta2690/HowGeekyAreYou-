package com.example.splash;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    int finalResult=0;

    TextView tv_Result,tv_resultText;
    ImageView iv_ResultImage;
    Button btn_quit, btn_TryAgain;
    ArrayList<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Results");
        //actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setIcon(R.mipmap.ic_launcher);

        tv_Result =  (TextView) findViewById(R.id.tv_Result);
        tv_resultText = (TextView)findViewById(R.id.tv_StatusText);
        iv_ResultImage =(ImageView)findViewById(R.id.iv_ImageStatus);
        btn_quit = (Button)findViewById(R.id.btn_Quit);
        btn_TryAgain = (Button)findViewById(R.id.btn_TryAgain);

        questionList = getIntent().getParcelableArrayListExtra("QUESTION_LIST"); //new ArrayList<>();
        finalResult = getIntent().getIntExtra("TOTAL_SCORE", 0);
        Log.d("Final Result:", " "+finalResult);

        if(finalResult<=10)
        {
            tv_Result.setText(R.string.non_geek);
            tv_resultText.setText("There isn't a single geeky bone in your body. You prefer to party rather than study,and have someone else fix your computer, if need be. You're just too cool for this. You probably don't even wear glasses!");//(R.string.non_geek_description);
            iv_ResultImage.setImageDrawable(getResources().getDrawable(R.drawable.non_geek));
         }
        else if(finalResult>11 && finalResult<50)
        {
            tv_Result.setText(R.string.semi_geek);
            tv_resultText.setText("Maybe you're just influenced by the trend, or maybe you just got it all perfectly balanced. You have some geeky traits, but they aren't as \"hardcore\" and they don't take over your life. You like some geeky things, but aren't nearly as obsessive about them as the uber-geeks. You actually get to enjoy both worlds");//(R.string.semi_geek_description);
            if (Build.VERSION.SDK_INT >= 21)
                iv_ResultImage.setImageDrawable(getResources().getDrawable(R.drawable.semi_geek,null));
            else
                iv_ResultImage.setImageDrawable(getResources().getDrawable(R.drawable.semi_geek));
        }
        else if(finalResult>51 && finalResult<72)
        {
            tv_Result.setText(R.string.uber_geek);
            tv_resultText.setText("You are the geek supreme! You are likely to be interested in technology, science, gaming and geeky media such as Sci-Fi and fantasy. All the mean kids that used to laugh at you in high school are now begging you for a job. Be proud of your geeky nature, for geeks shall inherit the Earth!");//(R.string.uber_geek_description);
            iv_ResultImage.setImageDrawable(getResources().getDrawable(R.drawable.uber_geek));
        }


        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this,WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this,QuizActivity.class);
                i.putParcelableArrayListExtra("QUESTION_LIST",questionList);
                startActivity(i);
                finish();
            }
        });
    }//end of OnCreate
}
