package com.example.sistempakarpasalkdrt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class kelompok extends AppCompatActivity {

    Button kem ;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelompok);

        setTitle("Anggota Kelompok");
        session = new SessionHandler(getApplicationContext());



    }
}