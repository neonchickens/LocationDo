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
import java.util.regex.Matcher;
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
    EditText conf;

    private final String PASSWORD_REQUIREMENTS = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%]).{8,40})";

    private static final String DB_URL = "jdbc:jtds:sqlserver://3.87.197.166:1433/LocationDo;user=LocationDo;password=CitSsd!";
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
        if(!checkPassword(password)){
            return;
        }
        String strUsername = username.getText().toString();
        String strPassword = SHA512(password.getText().toString());
        if(password.getText().toString().equals(conf.getText().toString())){
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
        }else{
            Toast.makeText(this,"Passwords do not match", Toast.LENGTH_LONG).show();
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

    /* checks that password meets minimum requirements
     * @param editText
     */
    private boolean checkPassword(EditText editText){
        Pattern pattern = Pattern.compile(PASSWORD_REQUIREMENTS);
        String pw = password.getText().toString();

        Matcher matcher = pattern.matcher(pw);
        if(!matcher.matches()){
            Toast.makeText(this,"Password must be: \n 8 characters long \n Contain 1 number \n " +
                    "Contain 1 lower case letter \n Contain 1 upper case letter \n Contain one special character " +
                    "(!@#$%)", Toast.LENGTH_LONG).show();
            password.setText("");
        }

        return matcher.matches();

    }
}
