package com.example.sistempakarpasalkdrt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class pakar extends AppCompatActivity {
Button kem ;
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_pakar);

        setTitle("Pakar");





    }
}