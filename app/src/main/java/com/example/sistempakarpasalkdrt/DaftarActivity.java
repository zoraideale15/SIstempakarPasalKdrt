package com.example.sistempakarpasalkdrt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DaftarActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final String url = "https://sistempakarkdrt.000webhostapp.com/daftar.php";
    private EditText et_nama_lengkap;
    private EditText et_username;
    private EditText et_password;
    private EditText et_alamat;
    private EditText et_nohp;
    private EditText et_nama_pelaku;



    private String nama_lengkap;
    private String username;
    private String password;
    private String alamat;
    private String nohp;
    private String nmplaku;
private RequestQueue mQuew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        setTitle("Daftar User");
        mQuew = Volley.newRequestQueue(this);

        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_alamat = findViewById(R.id.et_alamat);
        et_nohp = findViewById(R.id.et_nohp);
        et_nama_pelaku = findViewById(R.id.et_nama_pelaku);

        Button login = findViewById(R.id.btn_login);
        Button daftar = findViewById(R.id.btn_daftar);

        daftar.setOnClickListener(v -> {
            nama_lengkap = et_nama_lengkap.getText().toString().trim();
            username = et_username.getText().toString().toLowerCase().trim();
            password = et_password.getText().toString().trim();
            alamat = et_alamat.getText().toString().trim();
            nohp = et_nohp.getText().toString().trim();
            nmplaku = et_nama_pelaku.getText().toString().trim();
            if (validateInputs()) {
                daftar();
            }
        });

        login.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private boolean validateInputs() {
        if (nama_lengkap.equals("")) {
            et_nama_lengkap.setError("Nama Lengkap tidak boleh kosong");
            et_nama_lengkap.requestFocus();
            return false;
        }
        if (username.equals("")) {
            et_username.setError("Username tidak boleh kosong");
            et_username.requestFocus();
            return false;
        }
        if (password.equals("")) {
            et_password.setError("Password tidak boleh kosong");
            et_password.requestFocus();
            return false;
        }
        if (alamat.equals("")) {
            et_alamat.setError("Alamat tidak boleh kosong");
            et_alamat.requestFocus();
            return false;
        }
        if (nohp.equals("")) {
            et_nohp.setError("No Hp tidak boleh kosong");
            et_nohp.requestFocus();
            return false;
        }
        if (nmplaku.equals("")) {
            et_nama_pelaku.setError("Nama Pelaku tidak boleh kosong");
            et_nama_pelaku.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DaftarActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void daftar() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("nama_lengkap", nama_lengkap);
            request.put("username", username);
            request.put("password", password);
            request.put("alamat", alamat);
            request.put("nohp", nohp);
            request.put("pelaku", nmplaku);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
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

}