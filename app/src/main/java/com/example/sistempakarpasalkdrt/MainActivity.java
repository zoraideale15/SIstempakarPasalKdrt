package com.example.sistempakarpasalkdrt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
ViewFlipper slide;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu Utama User");
        int images[]= {R.drawable.download, R.drawable.images,R.drawable.images2};

        session = new SessionHandler(getApplicationContext());

        Button btn_diagnosa_cf = findViewById(R.id.btn_diagnosa_cf);
        Button btn_riwayat = findViewById(R.id.btn_riwayat);
        Button btn_penyakit = findViewById(R.id.btn_penyakit);
        Button btn_logout = findViewById(R.id.btn_logout);
        Button btn_tentang = findViewById(R.id.btn_tentang);
        slide = findViewById(R.id.slide);

        for(int image : images){
            flipperImages(image);
        }




        btn_diagnosa_cf.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DiagnosaCfActivity.class);
            startActivity(intent);
        });

        btn_riwayat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });

        btn_penyakit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PasalActivity.class);
            startActivity(intent);
        });

        btn_tentang.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Tentang.class);
            startActivity(intent);
        });



        btn_logout.setOnClickListener(view -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin mau logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Logout", (dialog, whichButton) -> {
                    session.logoutUser();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Tidak", null).show());

    }
    public void flipperImages(int image){
        ImageView imageView= new ImageView(this);
        imageView.setBackgroundResource(image);
        slide.addView(imageView);
        slide.setFlipInterval(3000);
        slide.setAutoStart(true);
        slide.setInAnimation(this , android.R.anim.slide_in_left);
        slide.setOutAnimation(this , android.R.anim.slide_out_right);



    }

}