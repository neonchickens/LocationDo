package com.example.locationdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * This is the Registration Activity for the LocationDo app
 * Should allow user to register with a password that meets requirements.
 * Sends username and password to corresponding login EditText boxes if valid.
 * **PASSWORD VALIDATIION NEEDS FIXED IN hasVariedChar() THEN IMPLEMENTED IN transition()**
 */
public class Register extends AppCompatActivity {
    EditText username;
    EditText password;
    private static final String URL = "34.201.242.17";
    public static final String USERNAME = "com.example.android.CIT268.extra.USERNAME";
    public static final String PASSWORD = "com.example.android.CIT268.extra.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.enterUsername);
        password = findViewById(R.id.enterPassword);
    }

    /**
     * transition to log in with valid registration.
     * @param view
     */
    public void transition(View view) {
        String user = username.getText().toString();
        String pw = password.getText().toString();
        Intent returnIntent = new Intent();
        // if(checkPassword(password)) {
        returnIntent.putExtra(USERNAME, user);
        returnIntent.putExtra(PASSWORD, pw);
        setResult(RESULT_OK, returnIntent);
        finish();
        //}


    }

    /**
     * checks that password meets min requirements
     * @param editText
     */
    private boolean checkPassword(EditText editText){
        String pw;
        boolean valid = false;
        password = findViewById(R.id.enterPassword);
        pw = password.getText().toString();
        if(hasVariedChar(pw) && validLength(pw))
            valid = true;

        return valid;
    }
    /**
     * checks password meets character type requirements
     * I can't get the pattern to match. I'm not sure if I'm using the wrong method or if my regex's are wrong. Should toast only when
     * password  doesn't have a special character and a number
     */
    private boolean hasVariedChar(String str){
        boolean valid = false;

        if(Pattern.matches("\\d", str) && Pattern.matches("[^A-Za-z0-9]", str)) {
            valid = true;
        } else {
            password.clearComposingText();
            Toast toast = Toast.makeText(getApplicationContext(), "Password must include one number and a special character", Toast.LENGTH_LONG);
            toast.show();
        }
        return valid;
    }

    /**
     * checks that password is of sufficient length
     * @param str
     * @return
     */
    private boolean validLength(String str){
        boolean valid = false;

        if(str.length() >= 8)
            valid  = true;
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_LONG);
            toast.show();
        }
        return valid;
    }
}
