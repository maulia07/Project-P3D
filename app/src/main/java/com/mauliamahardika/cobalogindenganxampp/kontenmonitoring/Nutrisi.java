package com.mauliamahardika.cobalogindenganxampp.kontenmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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
import com.brkckr.circularprogressbar.CircularProgressBar;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.MainActivity;
import com.mauliamahardika.cobalogindenganxampp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Nutrisi extends AppCompatActivity {
    RequestQueue requestQueue;
    ImageView back;
    private static final String URL_BACANUTRISI = "https://p3d.himauntika.com/app/bacanutrisi.php";
    private static final String URL_KONTROL = "https://p3d.himauntika.com/app/kontrol.php";
    private static final String URL_BACAMESIN = "https://p3d.himauntika.com/app/mesinnutrisi.php";
    private static final String TAG = MasaTanam.class.getSimpleName(); //getting the info

    //deklarasi komponen
    CircularProgressBar ciclenutrisi;
    TextView vnutrisi,vstatus,vtglupdate,vjamupdate;

    //deklarasi komponen voice
    private TextToSpeech tts;
    private ArrayList<String> questions;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";

    ImageView btnvoiceph;
    ImageView keluaranmesin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrisi);
        //inisialisasi komponennya
        ciclenutrisi=findViewById(R.id.txtcirclenutrisi);
        vnutrisi=findViewById(R.id.bacanutrisi);
        vstatus=findViewById(R.id.txtstatus);
        vtglupdate=findViewById(R.id.txttanggalupdate);
        vjamupdate=findViewById(R.id.txtjamupdate);

        //mesin
        keluaranmesin=findViewById(R.id.outpunmesin);



        ///deklarasi komponen voice
        btnvoiceph=findViewById(R.id.inputnutrisi);
        btnvoiceph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen();
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }



                } else {
                    Log.e("TTS", "Initilization Failed!");

                }
            }
        });


        //buat kembali
        back=findViewById(R.id.backarrow);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));
        //reload untuk komponen
        bacanutrisi();
        outmesin();
        /*Timer timer=new Timer();
        TimerTask tasknew=new TimerTask() {
            @Override
            public void run() {
                Nutrisi.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bacanutrisi();
                        outmesin();
                    }
                });

            }
        };timer.scheduleAtFixedRate(tasknew,0,100000);*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaddua();
                Intent i=new Intent(Nutrisi.this, IndexMenu.class);
                startActivity(i);
                finish();
            }
        });
    }

    //mesin
    private void outmesin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BACAMESIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("kontrolnutrisi").trim();

                                    if (strName.equals("1")){
                                        keluaranmesin.setImageResource(R.drawable.ic_baseline_wb_sunny_24_hidupmesin);
                                    }else if (strName.equals("0")){
                                        keluaranmesin.setImageResource(R.drawable.ic_baseline_wb_sunny_24_matimesin);
                                    }
                                }
                            }

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                           // progressDialog.dismiss();
                           // Toast.makeText(Nutrisi.this, "Error Reading Detail " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                      //  Toast.makeText(Nutrisi.this, "Error Reading Detail " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            requestQueue.add(stringRequest);
        }
    }

    //komponen voice
    private void listen() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan 'Tambahkan Nutrisi'");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(Nutrisi.this, "Perangkat Anda Tidak Mendukung Untuk Suara", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);


        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        if(text.contains("hello")){
            speak(questions.get(0));

        }
        //


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu Yaaa....");
        progressDialog.setCanceledOnTouchOutside(false);
       if(text.contains("tambahkan nutrisi")){
            final MediaPlayer mp7 = MediaPlayer.create(this, R.raw.nutrisitambah);
            final MediaPlayer mp8 =  MediaPlayer.create(this, R.raw.prosestambahnutrisi);
            uploadsatu();
            mp7.start();
            progressDialog.show();
            btnvoiceph.setEnabled(false);
            btnvoiceph.setImageResource(R.drawable.ic_baseline_keyboard_voice_mati_24);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    uploaddua();
                    mp8.start();
                    btnvoiceph.setEnabled(true);
                    btnvoiceph.setImageResource(R.drawable.ic_baseline_keyboard_voice_24);
                    progressDialog.dismiss();
                }
            }, 20000);


        }
    }

    //upload printah
    private void uploaddua() {


        final String kontrolnutrisi = "0";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_KONTROL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                progressDialog.dismiss();
                                //Toast.makeText(Nutrisi.this, "Berhasil Di Tambahkan!", Toast.LENGTH_SHORT).show();
                                //sessionManager.createSession(name, email, id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            //Toast.makeText(Nutrisi.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Nutrisi.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kontrolnutrisi", kontrolnutrisi);
                return params;
            }
        };

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            requestQueue.add(stringRequest);
        }
        
    }

    private void uploadsatu() {
        final String kontrolnutrisi = "1";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_KONTROL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(Nutrisi.this, "Berhasil Di Tambahkan!", Toast.LENGTH_SHORT).show();
                                //sessionManager.createSession(name, email, id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Nutrisi.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Nutrisi.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kontrolnutrisi", kontrolnutrisi);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //baca nutrisi
    private void bacanutrisi() {
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String j = (String) b.get("idnya");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BACANUTRISI,
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
                                String strKelembapan = object.getString("nutrisi").trim();
                                String strTanggal=object.getString("tanggal").trim();
                                String strJam=object.getString("jam").trim();
                                vnutrisi.setText(strKelembapan);

                                float b= Float.parseFloat(strKelembapan);
                                float c;
                                c=b/10;

                                ciclenutrisi.setProgressValue(c);
                                vtglupdate.setText(strTanggal);
                                vjamupdate.setText(strJam);

                                float a= Float.parseFloat(strKelembapan);

                                if (a>700){
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
uploaddua();

        Intent i=new Intent(Nutrisi.this,IndexMenu.class);
        startActivity(i);
        finish();
    }
}