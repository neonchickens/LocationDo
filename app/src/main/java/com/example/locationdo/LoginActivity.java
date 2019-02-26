package com.example.locationdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * This is the login activity for LocationDo app.
 * It is missing code for checking username and password with database as well as a transition to the map after
 * login is verified. Includes an intent to register if user is not already registered
 */
public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = findViewById(R.id.enterUsername);
        password = findViewById(R.id.enterPassword);
    }

    /**
     * Method for the intent to log in to map activity
     * @param view
     */
    public void transition(View view) {
    }

    /**
     * Method for validating Username with database
     */
    private void validateUsername(EditText editText){
        String username;
        editText = findViewById(R.id.enterUsername);
        username = editText.getText().toString();
    }

    /**
     * Method for validating password with username's storesd password
     */
    private void validatePassword(EditText editText){

        editText = findViewById(R.id.enterPassword);
        char[] password = editText.getText().toString().toCharArray();


    }

    /**
     * sends user to register activity
     * @param view
     */
    public void register(View view) {
        Intent intent = new Intent(this, com.example.locationdo.Register.class);
        startActivityForResult(intent, 1);
    }

    /**
     * autofills the editText to include with the registered username and password
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                username.setText(data.getStringExtra(com.example.locationdo.Register.USERNAME));
                password.setText(data.getStringExtra(com.example.locationdo.Register.PASSWORD));
            }
        }

    }
}
