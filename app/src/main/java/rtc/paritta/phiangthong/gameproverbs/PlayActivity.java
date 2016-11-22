package rtc.paritta.phiangthong.gameproverbs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    //Explicit
    private TextView scoreTextView, timeTextView, timesTextView;
    private ImageView imageView;
    private Button choice0Button, choice1Button, choice2Button, choice3Button;
    private int timeAnInt = 0, timesAnInt = 1, scoreAnInt = 0,
            randomAnInt, lengthAnInt;
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

        //Show First View
        Random random = new Random();
        randomAnInt = random.nextInt(lengthAnInt);
        Log.d("gameV2", "randomAnInt ==> " + randomAnInt);
        showView();

        //My Loop
        myLoop();

        //Button Controller
        choice0Button.setOnClickListener(this);
        choice1Button.setOnClickListener(this);
        choice2Button.setOnClickListener(this);
        choice3Button.setOnClickListener(this);


    }   // Main Method

    private void showView() {

        choice0Button.setText(choice0Strings[randomAnInt]);
        choice1Button.setText(choice1Strings[randomAnInt]);
        choice2Button.setText(choice2Strings[randomAnInt]);
        choice3Button.setText(choice3Strings[randomAnInt]);

        Picasso.with(PlayActivity.this)
                .load(imageStrings[randomAnInt])
                .resize(500, 450)
                .into(imageView);


    } // showView

    private void synQuestion() {

        try {

            SynQuestion synQuestion = new SynQuestion(PlayActivity.this);
            synQuestion.execute();
            jsonString = synQuestion.get();
            Log.d("gameV1", "JSON ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);

            lengthAnInt = jsonArray.length();

            //จองหน่วยความจำ
            imageStrings = new String[jsonArray.length()];
            choice0Strings = new String[jsonArray.length()];
            choice1Strings = new String[jsonArray.length()];
            choice2Strings = new String[jsonArray.length()];
            choice3Strings = new String[jsonArray.length()];
            answerStrings = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                imageStrings[i] = jsonObject.getString("Image");
                choice0Strings[i] = jsonObject.getString("Choice0");
                choice1Strings[i] = jsonObject.getString("Choice1");
                choice2Strings[i] = jsonObject.getString("Choice2");
                choice3Strings[i] = jsonObject.getString("Choice3");
                answerStrings[i] = jsonObject.getString("Answer");

                Log.d("gameV2", "image(" + i + ") ==> " + imageStrings[i]);


            }   // for


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

    @Override
    public void onClick(View view) {

        Random random = new Random();
        randomAnInt = random.nextInt(lengthAnInt);
        Log.d("gameV2", "randomAnInt ==> " + randomAnInt);

        showView();


    }   // onClick

}   // Main Class
