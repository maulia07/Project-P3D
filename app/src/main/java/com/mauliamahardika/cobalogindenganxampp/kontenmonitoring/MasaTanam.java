package com.mauliamahardika.cobalogindenganxampp.kontenmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.MainActivity;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam.Produk;
import com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam.ProdukAdapter;
import com.mauliamahardika.cobalogindenganxampp.masatanam.IsiMasaTanam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.Method.GET;

public class MasaTanam extends AppCompatActivity {

    //url tanam
    private static final String URL_PRODUK = "http://192.168.0.3/hidroponik/bacaprediksiasli.php";
    List<Produk>produkList;
    ProdukAdapter adapter;
    RecyclerView recyclerView;
    private static final String TAG = MasaTanam.class.getSimpleName(); //getting the info


    FloatingActionButton addnya;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masa_tanam);
        //



        //inisialisasi
        addnya=findViewById(R.id.btnadd);
        back=findViewById(R.id.backarrow);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MasaTanam.this,IndexMenu.class);
                startActivity(i);
                finish();
            }
        });
        //status bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));
        //get id usernya
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j =(String) b.get("idnya");


        addnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MasaTanam.this, IsiMasaTanam.class);
                i.putExtra("idnya",j);
                startActivity(i);
                finish();
            }
        });

        //inisialisasi buat recyleview
        recyclerView =findViewById(R.id.recyclevieww);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //inisiliasi konten recyclenya
        produkList=new ArrayList<>();
        LoadMasaTanam();


    //Toast.makeText(this,"LADA"+j,Toast.LENGTH_LONG).show();
    }

    private void LoadMasaTanam() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j = (String) b.get("idnya");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                //Toast.makeText(MasaTanam.this,"Respon",Toast.LENGTH_LONG).show();

                                //getting product object from json array
                                JSONObject produk=array.getJSONObject(i);
                                    produkList.add(new Produk(
                                            produk.getInt("seri"),
                                            produk.getInt("id"),
                                            produk.getString("namapenanam"),
                                            produk.getString("jenistanaman"),
                                            produk.getString("jumlahtanaman"),
                                            produk.getString("tglsemai"),
                                            produk.getString("prediksiparalon"),
                                            produk.getString("prediksipanen"),
                                            produk.getString("estimasipenjualan")));

                            }

                            //creating adapter object and setting it to recyclerview
                            ProdukAdapter adapter = new ProdukAdapter(MasaTanam.this, produkList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();

                // Toast.makeText(MasaTanam.this,"Read input"+j,Toast.LENGTH_LONG).show();
                params.put("id", j);
                return params;
            }
        };


        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    /*private void LoadMasaTanam() {
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j =(String) b.get("idnya");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    //JSONObject object = jsonArray.getJSONObject(i);

                                    //String strName = object.getString("name").trim();
                                    //String hh="apa";
                                    //String strEmail = object.getString("email").trim();
                                    //m.setText(strName);
                                    //email.setText(strEmail);

                                    Toast.makeText(MasaTanam.this,"Berhasil Baca",Toast.LENGTH_LONG).show();

                                    JSONObject produk=jsonArray.getJSONObject(i);
                                    produkList.add(new Produk(
                                           // produk.getInt("seri"),
                                            //produk.getInt("id"),
                                            produk.getString("namapenanam"),
                                            produk.getString("jenistanaman"),
                                            produk.getString("jumlahtanaman"),
                                            produk.getString("tglsemai")));

                                }
                                adapter=new ProdukAdapter(MasaTanam.this,produkList);
                                recyclerView.setAdapter(adapter);

                            }else {
                                Toast.makeText(MasaTanam.this,"Gagal Baca",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(MasaTanam.this, "Error 1 Detail "+e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MasaTanam.this, "Error 2 Detail "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();

               // Toast.makeText(MasaTanam.this,"Read input"+j,Toast.LENGTH_LONG).show();
                params.put("id", j);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

    @Override
    public void onBackPressed() {
        //  moveTaskToBack(true);

        Intent i=new Intent(MasaTanam.this,IndexMenu.class);
        startActivity(i);
        finish();
    }
}