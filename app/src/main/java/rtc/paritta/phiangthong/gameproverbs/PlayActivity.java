package rtc.paritta.phiangthong.gameproverbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    //Explicit
    private TextView scoreTextView, timeTextView, timesTextView;
    private ImageView imageView;
    private Button choice0Button, choice1Button, choice2Button, choice3Button;
    private int timeAnInt = 0, timesAnInt = 1, scoreAnInt = 0;
    private boolean timeABoolean = false;   // false ==> ยังไม่หมดเวลา 120 วินาที
    private String jsonString;
    private String[] imageStrings, choice0Strings, choice1Strings,
            choice2Strings, choice3Strings, answerStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Bind Widget
        scoreTextView = (TextView) findViewById(R.id.textView);
        timeTextView = (TextView) findViewById(R.id.textView2);
        timesTextView = (TextView) findViewById(R.id.textView3);
        imageView = (ImageView) findViewById(R.id.imageView3);
        choice0Button = (Button) findViewById(R.id.button);
        choice1Button = (Button) findViewById(R.id.button2);
        choice2Button = (Button) findViewById(R.id.button3);
        choice3Button = (Button) findViewById(R.id.button4);

        //Syn Question From Server
        synQuestion();

        //My Loop
        myLoop();


    }   // Main Method

    private void synQuestion() {

        try {

            SynQuestion synQuestion = new SynQuestion(PlayActivity.this);
            synQuestion.execute();
            jsonString = synQuestion.get();
            Log.d("gameV1", "JSON ==> " + jsonString);


        } catch (Exception e) {
            Log.d("gameV1", "e synQuestion ==> " + e.toString());
        }


    }   // synQuestion

    private void myLoop() {
        //*****************
        // To Do
        //*****************


        //Show Time
        timeTextView.setText("เวลา " + Integer.toString(120 - timeAnInt) + " วินาที");
        timeAnInt += 1;
        if (timeAnInt == 120) {
            timeABoolean = true;
        }   // if


        //Post Delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!timeABoolean) {
                    myLoop();
                } else {
                    //หมดเวลา 120 sec
                    Intent intent = new Intent(PlayActivity.this, ShowScoreActivity.class);
                    intent.putExtra("Score", scoreAnInt);
                    startActivity(intent);
                    finish();
                }   // if

            }   //run
        }, 1000);

    }   // myLoop

}   // Main Class
