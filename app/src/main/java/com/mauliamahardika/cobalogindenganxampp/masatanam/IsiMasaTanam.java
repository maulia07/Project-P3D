package com.mauliamahardika.cobalogindenganxampp.masatanam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.MasaTanam;
import com.mauliamahardika.cobalogindenganxampp.registrasi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IsiMasaTanam extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static String URL_PREDIKSI = "http://himauntika.com/hidroponikp3d/masatanam.php";

    private ProgressBar loading;
    EditText hasiledittext;
    ImageView plhclndr,backdua;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //komponen input
    MaterialButton btninput;
    TextInputEditText vnama,vjumlahtanam;
    Spinner jnstaneman, kelompok;
    EditText tgltanem;
    TextInputEditText prdkparalon,prdkpanen,prdkpenjualan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_masa_tanam);

        //status bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.warna_statusbar));

        //inisialisasi
        hasiledittext=findViewById(R.id.txttanggalpilihan);
        backdua=findViewById(R.id.backarrowdua);
        plhclndr=findViewById(R.id.pilihdarikalender);

        //deklarasi komponen input dan prediksi
        //input
        vnama=findViewById(R.id.txtnamapenanam);
        kelompok=findViewById(R.id.txtklmpk);
        vjumlahtanam=findViewById(R.id.txtjumlahtanam);
        jnstaneman=findViewById(R.id.txtjnstaneman);
        tgltanem=findViewById(R.id.txttanggalpilihan);
        //prediksi
        prdkparalon=findViewById(R.id.txtprediksiparalon);
        prdkpanen=findViewById(R.id.txtpanen);
        prdkpenjualan=findViewById(R.id.txtestimasipenjualan);
        //submit
        btninput=findViewById(R.id.btnsubmitall);

        //fungsi account
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j =(String) b.get("idnya");

        //fungsi tombol input ke database
        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input
                String mName = vnama.getText().toString().trim();
                String mKelompok = kelompok.getSelectedItem().toString().trim();
                String mJnstanem=jnstaneman.getSelectedItem().toString().trim();
                String mJumlahteneman=vjumlahtanam.getText().toString().trim();
                String mTgltenm=tgltanem.getText().toString().trim();
                //prediksi
                String mPrdkparalon=prdkparalon.getText().toString().trim();
                String mPrdkpanen=prdkpanen.getText().toString().trim();
                String mPrdkpenjualan=prdkpenjualan.getText().toString().trim();


                if (!mName.isEmpty() && !mKelompok.equals("--Pilih--") && !mJumlahteneman.isEmpty() && !mTgltenm.isEmpty() && !mJnstanem.equals("--Pilih--")) {
                    //Toast.makeText(IsiMasaTanam.this,"Berhasil",Toast.LENGTH_LONG).show();
                    masukprediksi();

                } else if(mName.isEmpty()) {
                    Toast.makeText(IsiMasaTanam.this, "Nama Belum Di Isi", Toast.LENGTH_LONG).show();

                } else if (mKelompok.equals("--Pilih--")){
                    Toast.makeText(IsiMasaTanam.this, "Kelompok Masyarakat Belum Di Pilih",Toast.LENGTH_LONG).show();

                }else if (mJnstanem.equals("--Pilih--")){
                    Toast.makeText(IsiMasaTanam.this,"Jenis Tanaman Belum Di pilih",Toast.LENGTH_LONG).show();

                }else if(mJumlahteneman.isEmpty()){
                    Toast.makeText(IsiMasaTanam.this,"Jumlah Tanaman Belum Di Isi",Toast.LENGTH_LONG).show();

                } else if (mTgltenm.isEmpty()){
                    Toast.makeText(IsiMasaTanam.this,"Tanggal Tanam Belum Di Isi",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(IsiMasaTanam.this,"Isi Semua Instrumen",Toast.LENGTH_LONG).show();

                }




            }
        });




        //input calender
        plhclndr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        IsiMasaTanam.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
               tambahcalender();
            }
        });
        //nampilin kalender ke edittext

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year+ "-" + month + "-" + dayOfMonth);

                String date = year + "-" +month + "-" + dayOfMonth;
                hasiledittext.setText(date);

                int hari=Integer.valueOf(dayOfMonth);
                int bulan=Integer.valueOf(month);

                tambahcalender();
                /*Calendar k = Calendar.getInstance();
                k.add(Calendar.DATE, 40);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                String output = sdf1.format(k.getTime());
                prdkparalon.setText(output);*/

            }
        };

        //kalender set waktu sekarang
        Calendar klndr= Calendar.getInstance();
        SimpleDateFormat fklndr = new SimpleDateFormat("yyyy-MM-dd");
        String keluaran = fklndr.format(klndr.getTime());
        tgltanem.setText(keluaran);








        //fungsi pengolahan
            Olahjenistaneman();
            pengolahjumlahtanem();
        //fungsi back
        backdua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IsiMasaTanam.this, MasaTanam.class);
                i.putExtra("idnya",j);
                startActivity(i);
                finish();
            }
        });

    }

    //back android
    @Override
    public void onBackPressed() {
        //  moveTaskToBack(true);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j =(String) b.get("idnya");
        Intent i=new Intent(IsiMasaTanam.this, MasaTanam.class);
        i.putExtra("idnya",j);
        startActivity(i);
        finish();
    }


    //pengolahan spiner
    private void Olahjenistaneman(){


        jnstaneman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               tambahcalender();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    //fungsi penjumlahan saat isi kotak jumlah tanaman
    private void pengolahjumlahtanem(){
        vjumlahtanam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tambahcalender();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //fungsi start elemen keseluruhan
    private void tambahcalender(){

        String a= String.valueOf(vjumlahtanam.getText());
        if (jnstaneman.getSelectedItem().toString().trim().equals("Kangkung")){
            //prediksi pindah paralon
            String dt=tgltanem.getText().toString();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Calendar k = Calendar.getInstance();
            try {
                k.setTime(sdf1.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            k.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            String output = sdf1.format(k.getTime());
            prdkparalon.setText(output);
            //prediksi panen
            Calendar s = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                s.setTime(sdf2.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.add(Calendar.DATE, 21);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            String output2 = sdf2.format(s.getTime());
            prdkpanen.setText(output2);
            if (a.equals("")){
            } else {
                String b=vjumlahtanam.getText().toString();
                Float c= Float.valueOf(b);
                float kangkung=5000;
                float tanem=(c/5)*kangkung;
                //Toast.makeText(IsiMasaTanam.this,"Hasilnya "+tanem,Toast.LENGTH_LONG).show();
                prdkpenjualan.setText(String.valueOf(tanem));
                prdkpanen.setText(output2);

                //buat penambah kalender

                //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


            }

        }else if (jnstaneman.getSelectedItem().toString().trim().equals("Pakcoy")){
            String dt=tgltanem.getText().toString();
            Calendar k = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                k.setTime(sdf1.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            k.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

            String output = sdf1.format(k.getTime());
            //prediksi panen

            Calendar s = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                s.setTime(sdf2.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            s.add(Calendar.DATE, 35);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            String output2 = sdf2.format(s.getTime());
            prdkparalon.setText(output);
            prdkpanen.setText(output2);
            if(a.equals("")){
            }else {
                String b=vjumlahtanam.getText().toString();
                Float c= Float.valueOf(b);
                float pakcoy=5000;
                float tanem=c*pakcoy;
                //Toast.makeText(IsiMasaTanam.this,"Hasilnya "+tanem,Toast.LENGTH_LONG).show();
                prdkpenjualan.setText(String.valueOf(tanem));

                //buat penambah kalender

                //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


                prdkparalon.setText(output);
                prdkpanen.setText(output2);
            }
            // Toast.makeText(IsiMasaTanam.this,"Pakcoy",Toast.LENGTH_LONG).show();

        }else {
            //Toast.makeText(IsiMasaTanam.this,"gagal",Toast.LENGTH_LONG).show();

            prdkpenjualan.setText("0");
        }
    }//



    //koneksi Prediksi
    private void masukprediksi(){

//        loading.setVisibility(View.VISIBLE);
        //btn_regist.setVisibility(View.GONE);

        final String mName = this.vnama.getText().toString().trim();
        final String mKelompok = this.kelompok.getSelectedItem().toString().trim();
        final String mJnstanem=this.jnstaneman.getSelectedItem().toString().trim();
        final String mJumlahteneman=this.vjumlahtanam.getText().toString().trim();
        final String mTgltenm=this.tgltanem.getText().toString().trim();
        //prediksi
        final String mPrdkparalon=this.prdkparalon.getText().toString().trim();
        final String mPrdkpanen=this.prdkpanen.getText().toString().trim();
        final String mPrdkpenjualan=this.prdkpenjualan.getText().toString().trim();

        //final String name = this.name.getText().toString().trim();
        //final String email = this.email.getText().toString().trim();
      //  final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PREDIKSI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        //fungsi account
                        Intent iin= getIntent();
                        Bundle b = iin.getExtras();
                        String j =(String) b.get("idnya");
                        Toast.makeText(IsiMasaTanam.this, "Input Berhasil!", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(IsiMasaTanam.this,MasaTanam.class);
                        i.putExtra("idnya",j);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(IsiMasaTanam.this, "Input Gagal!", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(IsiMasaTanam.this, "Inputnya Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                    //g.setVisibility(View.GONE);
                    //btn_regist.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IsiMasaTanam.this, "Inputnya Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                     //   loading.setVisibility(View.GONE);
                        //btn_regist.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Intent iin= getIntent();
                Bundle b = iin.getExtras();
                String j =(String) b.get("idnya");
                params.put("id",j);
                params.put("namapenanam",mName);
                params.put("kelompokmasyarakat",mKelompok);
                params.put("jenistanaman",mJnstanem);
                params.put("jumlahtanaman",mJumlahteneman);
                params.put("tglsemai",mTgltenm);
                params.put("prediksiparalon",mPrdkparalon);
                params.put("prediksipanen",mPrdkpanen);
                params.put("estimasipenjualan",mPrdkpenjualan);
                //params.put("name", name);
                // params.put("email", email);
                //params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}