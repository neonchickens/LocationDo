package com.example.locationdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, com.example.locationdo.LoginActivity.class);
        startActivity(intent);
    }

    /*
    temporary way of getting to the list activity,
    replace with login later
     */
    public void toList(View view){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}