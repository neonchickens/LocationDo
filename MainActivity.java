package com.example.threenumbers;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Mike Moran
 * CIT 382 2/8/19
 * This is the main activity for a game that requires the user to guess three numbers then returns a
 * score based on whether each number matches a random number.
 */
public class MainActivity extends AppCompatActivity {
    public static final String NUMBER1 =
            "com.example.android.threenumbers.extra.NUMBER1";
    public static final String NUMBER2 =
            "com.example.android.threenumbers.extra.NUMBER2";
    public static final String NUMBER3 =
            "com.example.android.threenumbers.extra.NUMBER3";
    public static final int TOTAL_REQUEST = 1;

    private EditText userNumber1;
    private EditText userNumber2;
    private EditText userNumber3;
    private TextView totalScore;
    private TextView totalText;
    private int runningTotal;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userNumber1 = findViewById(R.id.number_box_one);
        userNumber2 = findViewById(R.id.number_box_two);
        userNumber3 = findViewById(R.id.number_box_three);
        totalScore = findViewById(R.id.total_score);
        totalText = findViewById(R.id.total_text);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        checkInt(userNumber1);
        checkInt(userNumber2);
        checkInt(userNumber3);
    }

    public void launchScoreActivity(View view) {

        Intent intent = new Intent(this, Score_Activity.class);

        String number1 = userNumber1.getText().toString();

        String number2 = userNumber2.getText().toString();

        String number3 = userNumber3.getText().toString();

        intent.putExtra(NUMBER1, number1);
        intent.putExtra(NUMBER2, number2);
        intent.putExtra(NUMBER3, number3);

        startActivityForResult(intent, TOTAL_REQUEST);
    }
    public void closeButton(View view){
        finish();
    }

    public boolean checkInt(EditText et){
        String number = et.getText().toString();
        if (et.length() > 1) {
            Toast toast = Toast.makeText(getApplicationContext(),"Number must be 0-9", Toast.LENGTH_LONG);
            toast.show();
            et.setText("");
            return false;
        }
        return true;

    }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TOTAL_REQUEST){
            if(resultCode == RESULT_OK){
                String total = data.getStringExtra(Score_Activity.TOTAL_SCORE);
                runningTotal = runningTotal + Integer.parseInt(total);
                totalText.setVisibility(View.VISIBLE);
                totalScore.setVisibility(View.VISIBLE);
                totalScore.setText(Integer.toString(runningTotal));
            }
        }

         checkWin(runningTotal);
     }
    public void checkWin (int i){
        if(i >= 20){
            totalText.setText("You Win!");
            userNumber1.setVisibility(View.INVISIBLE);
            userNumber2.setVisibility(View.INVISIBLE);
            userNumber3.setVisibility(View.INVISIBLE);
            btn = findViewById(R.id.submit_button);
            btn.setVisibility(View.INVISIBLE);
            btn = findViewById(R.id.close_button);
            btn.setVisibility(View.VISIBLE);
            totalText.setTextColor(Color.GREEN);
            totalText.setTextSize(35);
        }
    }
}