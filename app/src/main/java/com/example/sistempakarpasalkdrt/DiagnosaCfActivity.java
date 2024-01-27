package com.example.sistempakarpasalkdrt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DiagnosaCfActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sistempakarkdrt.000webhostapp.com/get_daftar_gejala.php";
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<Double> hasil;
    private TextView text_pertanyaan;
    private RadioGroup rg;
    private RadioButton rb;
    private int counter;
    private final int REQUEST_CODE = 1234;
    private Button diagnosa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf);
        setTitle("Diagnosa Pasal-Pasal");

        getData();

        text_pertanyaan = findViewById(R.id.text_pertanyaan);
        rg = findViewById(R.id.rg);

        Button btn_lanjut = findViewById(R.id.btn_lanjut);
        diagnosa = findViewById(R.id.diagnosis);

        btn_lanjut.setOnClickListener(view -> {
            int selected = rg.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Pilih dulu salah satu jawaban", Toast.LENGTH_SHORT).show();
            } else {
                rb = findViewById(selected);
                hasil.add(konversiJawaban(rb.getText().toString()));
                showPertanyaan(++counter);
            }
        });

        diagnosa.setOnClickListener(view -> {
            if (hasil.size() >= 1) {
                hitungDiagnosa();
            } else {
                Toast.makeText(this, "Pilih minimal 1 Tindakan terlebih dahulu", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void hitungDiagnosa() {
        Intent intent = new Intent(getApplicationContext(), DiagnosaCfHasilActivity.class);
        intent.putExtra("hasil", android.text.TextUtils.join("#", hasil));
        startActivityForResult(intent, REQUEST_CODE);
    }



    private double konversiJawaban(String value) {
        switch (value) {
            case "Tidak":
                return 0;
            case "Tidak yakin":
                return 0.2;
            case "Sedikit yakin":
                return 0.4;
            case "Cukup yakin":
                return 0.6;
            case "Yakin":
                return 0.8;
            case "Sangat yakin":
                return 1;
        }
        return 0;
    }

    private void showPertanyaan(int index) {
        if (index >= list.size() ){
            Intent intent = new Intent(getApplicationContext(), DiagnosaCfHasilActivity.class);
            intent.putExtra("hasil", android.text.TextUtils.join("#", hasil));
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            HashMap<String, String> item = list.get(index);

            // Tambahkan nomor pertanyaan
            String questionNumber = (index + 1) + "/" + list.size();
            TextView textViewNumber = findViewById(R.id.number);
            textViewNumber.setText(questionNumber);

            // Tambahkan teks pertanyaan
            text_pertanyaan.setText("Apakah " + item.get("nama_tindakan").toLowerCase() + "?");

            rg.clearCheck();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            getData();
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaCfActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            JSONArray jsonArray = response.getJSONArray("tindakan");
                            list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id_tindakan", jsonObject.getString("id_tindakan"));
                                map.put("nama_tindakan", jsonObject.getString("nama_tindakan"));
                                list.add(map);
                                kosong = false;
                            }
                            if (kosong) {
                                Toast.makeText(DiagnosaCfActivity.this, "Tidak ada data tindakan", Toast.LENGTH_SHORT).show();
                            } else {
                                hasil = new ArrayList<>();
                                counter = 0;
                                showPertanyaan(counter);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exitByBackKey();
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Anda yakin mau kembali ?")
                .setPositiveButton("Ya", (arg0, arg1) -> finish())
                .setNegativeButton("Tidak", (arg0, arg1) -> {
                })
                .show();
    }
}