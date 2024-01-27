package com.example.sistempakarpasalkdrt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DiagnosaCfHasilActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sistempakarkdrt.000webhostapp.com/get_hasil_cf.php";
    private String hasil;
    private Button btn_pasal,cswa,polis,pegacara;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf_hasil);
        setTitle("Hasil Diagnosa");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasil = extras.getString("hasil");
            ArrayList<HashMap<String, String>> tindakanlist = (ArrayList<HashMap<String, String>>) extras.getSerializable("tindakan_list");
        }

        btn_pasal = findViewById(R.id.btn_pasal);
        cswa = findViewById(R.id.CS_WA);

        polis = findViewById(R.id.polis);
        pegacara = findViewById(R.id.pengacara);
        Button logi = findViewById(R.id.refreshButton);
        Button user = findViewById(R.id.btn_dashboarduser);

        user.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), DiagnosaCfActivity.class);
            startActivity(i);
            finish();


        });
        cswa.setOnClickListener(view -> {
            String phoneNumber = "628111129129";  // Ganti dengan nomor telepon yang diinginkan
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        pegacara.setOnClickListener(view -> {
            String phoneNumber = "6281242882886";  // Ganti dengan nomor telepon yang diinginkan
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        polis.setOnClickListener(view -> {
            String phoneNumber = "110"; // Ganti dengan nomor yang diinginkan
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(dialIntent);
        });
        logi.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });

        getHasilDiagnosa();
    }
//    public void refreshWebView() {
//        webView.reload(); // Fungsi ini akan menyegarkan WebView
//    }
    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaCfHasilActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getHasilDiagnosa() {
        displayLoader();
        JSONObject request = new JSONObject();
        SessionHandler session = new SessionHandler(this);
        User user = session.getUserDetails();
        try {
            request.put("hasil", hasil);
            request.put("metode", "Certainty Factor");
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            if (response.getString("id_pasal").equals("")) {
                                TextView tv_title = findViewById(R.id.tv_title);
                                tv_title.setText(response.getString("nama_pasal"));
                                btn_pasal.setVisibility(View.GONE);
                            } else {
                                final String id_pasal = response.getString("id_pasal");
                                btn_pasal.setText(response.getString("nama_pasal") +
                                        " (" + response.getString("nilai") + "%)");
                                btn_pasal.setOnClickListener(v -> {
                                    Intent myIntent = new Intent(v.getContext(), PasalDetailActivity.class);
                                    myIntent.putExtra("id_pasal", id_pasal);
                                    startActivity(myIntent);
                                });
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
}