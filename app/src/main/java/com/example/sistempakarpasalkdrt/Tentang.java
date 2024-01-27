package com.example.sistempakarpasalkdrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class Tentang extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionHandler(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        setTitle("Tentang");
        TextView textView = findViewById(R.id.text);
        Button pakar = findViewById(R.id.pakar);
        Button kelompok = findViewById(R.id.klmpk);


        pakar.setOnClickListener( v -> {
            Intent intent = new Intent(Tentang.this, pakar.class);
            startActivity(intent);

        });
        kelompok.setOnClickListener( v -> {
            Intent intent = new Intent(Tentang.this, kelompok.class);
            startActivity(intent);

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}