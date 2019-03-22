package com.example.locationdo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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

    private static final String DB_URL = "jdbc:jtds:sqlserver://3.87.197.166:1433/LocationDo;user=LocationDo;password=CitSsd!";
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

        String strUsername = username.getText().toString();
        String strPassword = SHA512(password.getText().toString());

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL);

            Statement statement = con.createStatement();
            int result = statement.executeUpdate("INSERT INTO ACCOUNT (username, password) VALUES ('" + strUsername + "', '" + strPassword + "')");

            if (result == 1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(USERNAME, strUsername);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
