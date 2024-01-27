package com.example.sistempakarpasalkdrt;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PasalDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sistempakarkdrt.000webhostapp.com/get_penyakit.php";
    private TextView tv_nama_pasal;
    private TextView tv_deskripsi;
    private TextView tv_solusi;
    private String id_pasal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasal_detail);
        setTitle("Detail Pasal-Pasal");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_pasal = extras.getString("id_pasal");
        }

        tv_nama_pasal = findViewById(R.id.tv_nama_pasal);
        tv_deskripsi = findViewById(R.id.tv_deskripsi);
        tv_solusi = findViewById(R.id.tv_solusi);

        getPenyakit();
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

    private void getPenyakit() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_pasal", id_pasal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            tv_nama_pasal.setText(response.getString("nama_pasal"));
                            tv_deskripsi.setText(response.getString("deskripsi"));
                            tv_solusi.setText(response.getString("solusi"));
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

    private void displayLoader() {
        pDialog = new ProgressDialog(PasalDetailActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}