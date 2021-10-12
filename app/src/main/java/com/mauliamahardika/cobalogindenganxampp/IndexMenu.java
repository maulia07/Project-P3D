package com.mauliamahardika.cobalogindenganxampp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.BannerSlider;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.FragmentSlider;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.SliderIndicator;
import com.mauliamahardika.cobalogindenganxampp.kombanslide.SliderPagerAdapter;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.Kelembapan;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.MasaTanam;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.Nutrisi;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.SuhuAir;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.SuhuUdara;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.Tentang;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.VolumeAir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexMenu extends AppCompatActivity {
    //deklarasi
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private BannerSlider bannerSlider;
    private LinearLayout mLinearLayout;
    TextView m;
    private static final String TAG = IndexMenu.class.getSimpleName(); //getting the info
    private LinearLayout btn_logout, masatanam, suhuudara, kelembapan, nutrisi, suhuair, volumeair, tentang;
    SessionManager sessionManager;
    String getId;
    // private static String URL_UPLOAD = "http://192.168.0.3/android_register_login/upload.php";
    private static String URL_READ = "http://192.168.191.183/hidroponik/read_detail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_menu);

        checkInternetConnection();

        //inisialisasi
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        //inisialisasi perangkat
        m = findViewById(R.id.txtnamapenggunaa);
        btn_logout = findViewById(R.id.btnkeluar);
        masatanam = findViewById(R.id.txtmasatanam);
        suhuudara = findViewById(R.id.txtsuhuudara);
        kelembapan = findViewById(R.id.txtkelembapan);
        nutrisi = findViewById(R.id.txtnutrisi);
        suhuair = findViewById(R.id.txtsuhuair);
        volumeair = findViewById(R.id.txtvolumeair);
        tentang = findViewById(R.id.txttentang);


        //peralihan ke activity masa tanam
       /* masatanam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(IndexMenu.this, MasaTanam.class);

                startActivity(i);
            }
        });*/
        //fungsi keluar
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

        //status bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));

        //inisialisasi banner
        bannerSlider = findViewById(R.id.sliderView);
        mLinearLayout = findViewById(R.id.pagesContainer);
        setupSlider();

    }


    private void setupSlider() {
        bannerSlider.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();

        //link image
        fragments.add(FragmentSlider.newInstance("http://192.168.191.183/hidroponik/gambarslide/satu.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://192.168.191.183/hidroponik/gambarslide/dua.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://192.168.191.183/hidroponik/gambarslide/tiga.jpeg"));
        fragments.add(FragmentSlider.newInstance("http://192.168.191.183/hidroponik/gambarslide/empat.jpeg"));

        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
        bannerSlider.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(this, mLinearLayout, bannerSlider, R.drawable.ic_baseline_account_circle_24);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    private void getUserDetail() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    //String hh="apa";
                                    //String strEmail = object.getString("email").trim();
                                    m.setText(strName);
                                    masatanam.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(IndexMenu.this, MasaTanam.class);
                                            i.putExtra("idnya", getId);
                                            startActivity(i);
                                            finish();
                                            //
                                        }
                                    });

                                    suhuudara.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(IndexMenu.this, SuhuUdara.class);
                                            i.putExtra("idnya", getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    kelembapan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i= new Intent(IndexMenu.this, Kelembapan.class);
                                            i.putExtra("idnya",getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    nutrisi.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i= new Intent(IndexMenu.this, Nutrisi.class);
                                            i.putExtra("idnya",getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    suhuair.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i= new Intent(IndexMenu.this, SuhuAir.class);
                                            i.putExtra("idnya",getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    volumeair.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i= new Intent(IndexMenu.this, VolumeAir.class);
                                            i.putExtra("idnya",getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    tentang.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i= new Intent(IndexMenu.this, Tentang.class);
                                            i.putExtra("idnya",getId);
                                            startActivity(i);
                                            finish();
                                        }
                                    });


                                    //email.setText(strEmail);

                                }
                            }

                }

                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            //Toast.makeText(IndexMenu.this, "Error 1 " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(IndexMenu.this, "Eror " + error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        finish();
    }
    //cek koneksi
    private boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(IndexMenu.this);
            builder.setTitle("Tidak Ada Koneksi");
            builder.setMessage("Coba Periksa Status Koneksi Internet Anda !");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
           // builder.setOnCancelListener(true);
           dialog.setCanceledOnTouchOutside(false);

            return false;
        }
    }
}