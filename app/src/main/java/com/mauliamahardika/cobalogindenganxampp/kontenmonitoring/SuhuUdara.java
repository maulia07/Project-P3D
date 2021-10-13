package com.mauliamahardika.cobalogindenganxampp.kontenmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brkckr.circularprogressbar.CircularProgressBar;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam.Produk;
import com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam.ProdukAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

public class SuhuUdara extends AppCompatActivity {
    ImageView back;
    private static final String URL_BACASUHUUDARA = "http://himauntika.com/hidroponikp3d/bacasuhuudara.php";
    private static final String TAG = SuhuUdara.class.getSimpleName(); //getting the info

    //deklarasi komponen
    CircularProgressBar ciclesuhu;
    TextView vsuhuudara,vstatus,vtglupdate,vjamupdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu_udara);

        //inisialisasi komponennya
        ciclesuhu=findViewById(R.id.txtcirclesuhuudara);
        vsuhuudara=findViewById(R.id.bacasuhuudara);
        vstatus=findViewById(R.id.txtstatus);
        vtglupdate=findViewById(R.id.txttanggalupdate);
        vjamupdate=findViewById(R.id.txtjamupdate);



        //reload untuk komponen
        Timer timer=new Timer();
        TimerTask tasknew=new TimerTask() {
            @Override
            public void run() {
                SuhuUdara.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    bacasuhu();
                    }
                });
            }
        };timer.scheduleAtFixedRate(tasknew,0,1000);


        back=findViewById(R.id.backarrow);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SuhuUdara.this, IndexMenu.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void bacasuhu() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j = (String) b.get("idnya");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BACASUHUUDARA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {


                                //Toast.makeText(MasaTanam.this,"Respon",Toast.LENGTH_LONG).show();
                                JSONObject object = array.getJSONObject(i);

                                //getting product object from json array
                                String strSuhuUdara = object.getString("suhuudara").trim();
                                String strTanggal=object.getString("tanggal").trim();
                                String strJam=object.getString("jam").trim();
                                vsuhuudara.setText(strSuhuUdara);
                                ciclesuhu.setProgressValue(Float.parseFloat(strSuhuUdara));
                                vtglupdate.setText(strTanggal);
                                vjamupdate.setText(strJam);

                                float a= Float.parseFloat(strSuhuUdara);

                                if (a<25){
                                    vstatus.setText("Normal");
                                }else {
                                    vstatus.setText("Tidak Normal");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        //adding our stringrequest to queue
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

    }


    //bacpresed
    @Override
    public void onBackPressed() {
        //  moveTaskToBack(true);
        Intent i=new Intent(SuhuUdara.this,IndexMenu.class);
        startActivity(i);
        finish();
    }
}