package com.example.locationdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;
/**
 * Names: Jonas, Weston, Grant, Mike
 * Course: CIT368-01
 * Assignment: Group Part 2
 * Date: 4/28/2019
 * Purpose: This is the Registration Activity for the LocationDo app.
 *      Should allow user to register with a password that meets requirements.
 *      Sends username and password to corresponding login EditText boxes if valid.
 * Assumptions: Min SDK 23, Target SDK 28
 */

public class Register extends AppCompatActivity {
    EditText username;
    EditText password;
  
    EditText conf;
  
    public static final String USERNAME = "com.example.android.CIT268.extra.USERNAME";
    public static final String PASSWORD = "com.example.android.CIT268.extra.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.enterUsername);
        password = findViewById(R.id.enterPassword);
        conf = findViewById(R.id.confPassword);
    }

    /**
     * transition to log in with valid registration.
     * @param view
     */
    public void transition(View view) {

        String strUsername = username.getText().toString();
        String strPassword = SHA512(password.getText().toString());

        if(checkPassword(password.getText().toString()) && password.getText().toString().equals(conf.getText().toString())){
          try {
              String strStatement = "INSERT INTO ACCOUNT (username, password) VALUES (?, ?)";
              PreparedStatement psInsert = Settings.getInstance().getConnection().prepareStatement(strStatement);
              psInsert.setString(1, strUsername);
              psInsert.setString(2, strPassword);
              int result = psInsert.executeUpdate();
              if (result == 1) {
                  Intent returnIntent = new Intent();
                  returnIntent.putExtra(USERNAME, strUsername);
                  setResult(RESULT_OK, returnIntent);
                  finish();
              }

              psInsert.close();
          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(this, "Username already taken.", Toast.LENGTH_SHORT).show();
          }
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Hashing method using SHA512 for using the password
     * @param strPassword
     */
    private static String SHA512(String strPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(strPassword.getBytes(StandardCharsets.UTF_8));
            char[] hex = new char[digest.length * 2];
            for (int i = 0; i < digest.length; i++) {
                hex[2 * i] = "0123456789abcdef".charAt((digest[i] & 0xf0) >> 4);
                hex[2 * i + 1] = "0123456789abcdef".charAt(digest[i] & 0x0f);
            }
            return new String(hex);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * checks that password meets min requirements
     * @param strpw
     */
    public boolean checkPassword(String strpw){
        boolean valid = false;
        if(hasVariedChar(strpw) && validLength(strpw))
            valid = true;

        return valid;
    }
    /**
     * checks password meets character type requirements
     * Should toast only when
     * password  doesn't have a special character and a number
     */
    public boolean hasVariedChar(String str){
        boolean valid = false;

        if(str.matches("^.*\\d.*$") && str.matches("^.*\\W.*$")) {
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
    public boolean validLength(String str){
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
