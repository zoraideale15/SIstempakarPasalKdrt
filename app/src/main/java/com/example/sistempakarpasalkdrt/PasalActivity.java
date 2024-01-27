package com.example.sistempakarpasalkdrt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PasalActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://sistempakarkdrt.000webhostapp.com/get_daftar_penyakit.php";
    private ListView lv;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasal);
        setTitle("Daftar Pasal-Pasal");

        lv = findViewById(R.id.list_pasal);

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PasalActivity.this);
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
                            JSONArray jsonArray = response.getJSONArray("pasal");
                            ArrayList<HashMap<String, String>> list = new ArrayList<>();
                            boolean kosong = true;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id_pasal", jsonObject.getString("id_pasal"));
                                map.put("nama_pasal", jsonObject.getString("nama_pasal"));
                                list.add(map);
                                kosong = false;
                            }
                            adapter = new SimpleAdapter(
                                    PasalActivity.this,
                                    list,
                                    R.layout.pasal_list,
                                    new String[]{"id_pasal", "nama_pasal"},
                                    new int[]{R.id.id_pasal, R.id.nama_pasal});
                            lv.setAdapter(adapter);

                            AdapterView.OnItemClickListener itemClickListener = (parent, container, position, id) -> {
                                LinearLayout linearLayout = (LinearLayout) container;
                                TextView tv_id = (TextView) linearLayout.getChildAt(0);
                                Intent intent = new Intent(PasalActivity.this, PasalDetailActivity.class);
                                intent.putExtra("id_pasal", tv_id.getText().toString());
                                startActivity(intent);
                            };

                            lv.setOnItemClickListener(itemClickListener);

                            if (kosong) {
                                Toast.makeText(PasalActivity.this, "Tidak ada data penyakit",
                                        Toast.LENGTH_SHORT).show();
                            }

                            adapter.notifyDataSetChanged();
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
            onBackPressed();
            return true;
        }
        return false;
    }
}