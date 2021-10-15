package com.mauliamahardika.cobalogindenganxampp.kontenmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brkckr.circularprogressbar.CircularProgressBar;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Kelembapan extends AppCompatActivity {
    RequestQueue requestQueue;
    ImageView back;
    private static final String URL_BACAKELEMBAPAN = "https://himauntika.com/hidroponikp3d/bacakelembapan.php";
    private static final String TAG = MasaTanam.class.getSimpleName(); //getting the info

    //deklarasi komponen
    CircularProgressBar ciclekelembapan;
    TextView vkelembapan,vstatus,vtglupdate,vjamupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelembapan);
        //inisialisasi komponennya
        ciclekelembapan=findViewById(R.id.txtcirclekelembapan);
        vkelembapan=findViewById(R.id.bacakelembapan);
        vstatus=findViewById(R.id.txtstatus);
        vtglupdate=findViewById(R.id.txttanggalupdate);
        vjamupdate=findViewById(R.id.txtjamupdate);


        back=findViewById(R.id.backarrow);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));
        //reload untuk komponen
        Timer timer=new Timer();
        TimerTask tasknew=new TimerTask() {
            @Override
            public void run() {
                Kelembapan.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bacakelembapan();
                    }
                });

            }
        };timer.scheduleAtFixedRate(tasknew,0,1000);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Kelembapan.this, IndexMenu.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void bacakelembapan() {

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j = (String) b.get("idnya");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BACAKELEMBAPAN,
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
                                String strKelembapan = object.getString("kelembapan").trim();
                                String strTanggal=object.getString("tanggal").trim();
                                String strJam=object.getString("jam").trim();
                                vkelembapan.setText(strKelembapan);
                                ciclekelembapan.setProgressValue(Float.parseFloat(strKelembapan));
                                vtglupdate.setText(strTanggal);
                                vjamupdate.setText(strJam);

                                float a= Float.parseFloat(strKelembapan);

                                if (a<78){
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
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        //  moveTaskToBack(true);

        Intent i=new Intent(Kelembapan.this,IndexMenu.class);
        startActivity(i);
        finish();
    }
}