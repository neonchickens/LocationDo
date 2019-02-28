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

/**
 * This is the login activity for LocationDo app.
 * It is missing code for checking username and password with database as well as a transition to the map after
 * login is verified. Includes an intent to register if user is not already registered
 */
public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    private static final String DB_URL = "jdbc:jtds:sqlserver://34.201.242.17:1433/LocationDo;user=LocationDo;password=CitSsd!";

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

        String strUsername = username.getText().toString();
        String strPassword = SHA512(password.getText().toString());

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL);

            Statement statement = con.createStatement();
            ResultSet resultat = statement.executeQuery("SELECT ID FROM ACCOUNT WHERE USERNAME = '" + strUsername + "' and PASSWORD = '" + strPassword + "'");

            while (resultat.next()) {
                int id = resultat.getInt("id");
                if (id != -1) {
                    Toast.makeText(this,"Success", Toast.LENGTH_LONG);
                    //TODO
                    //Switch to list activity
                    //Pass id for sql
                } else {
                    Toast.makeText(this,"Failure", Toast.LENGTH_LONG);
                }

            }
            resultat.close();
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
     * Method for validating Username with database
     */
    private void validateUsername(EditText editText){

        String strUsername = editText.getText().toString();
    }

    /**
     * Method for validating password with username's storesd password
     */
    private void validatePassword(EditText editText){

        //String password = editText.getText().toString();
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
            }
        }

    }
}
