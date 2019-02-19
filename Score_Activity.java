package com.example.threenumbers;

import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Score_Activity extends AppCompatActivity {
    public static final String TOTAL_SCORE =
            "com.example.android.threenumbers.extra.TOTAL_SCORE";
    private TextView tvtotal;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_);

        Intent intent = getIntent();
        String number1 = intent.getStringExtra(MainActivity.NUMBER1);
        String number2 = intent.getStringExtra(MainActivity.NUMBER2);
        String number3 = intent.getStringExtra(MainActivity.NUMBER3);
        TextView textview1 = findViewById(R.id.num_return_1);
        TextView textview2 = findViewById(R.id.num_return_2);
        TextView textview3 = findViewById(R.id.num_return_3);
        tvtotal = findViewById(R.id.round_score);
        textview1.setText(number1);
        textview2.setText(number2);
        textview3.setText(number3);


        rightGuess(textview1);

        rightGuess(textview2);

        rightGuess(textview3);

        matchMessage(total);

        total = (total*total) * 10;
        tvtotal.setText(Integer.toString(total));
    }

    public void rightGuess(TextView textView){
        int number = Integer.parseInt(textView.getText().toString());
        int random = (int) (Math.random() * 10);

        if(number == random){
            textView.setTextColor(Color.GREEN);
            total = total + 1;
        }
        else
            textView.setTextColor(Color.RED);
    }

    public void matchMessage(int x){
        TextView textView = findViewById(R.id.score_notify);
        switch (x) {
            case 0:
                textView.setText("Sorry, there are no matches");
                break;
            case 1:
                textView.setText("There is 1 match");
                break;
            case 2:
                textView.setText("There are 2 matches");
                break;
            case 3:
                textView.setText("There are 3 matches");
                break;
        }

    }
    public void returnScore (View view){
        String score = tvtotal.getText().toString();
        Intent iScore = new Intent();
        iScore.putExtra(TOTAL_SCORE, score);
        setResult(RESULT_OK, iScore);
        finish();
    }

}
