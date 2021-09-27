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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.MasaTanam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IsiMasaTanam extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText hasiledittext;
    ImageView plhclndr,backdua;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //komponen input
    TextInputEditText vnama,vjumlahtanam;
    Spinner jnstaneman;
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

        hasiledittext=findViewById(R.id.txttanggalpilihan);
        backdua=findViewById(R.id.backarrowdua);
        plhclndr=findViewById(R.id.pilihdarikalender);

        //deklarasi komponen input dan prediksi
        //input
        vnama=findViewById(R.id.txtnamapenanam);
        vjumlahtanam=findViewById(R.id.txtjumlahtanam);
        jnstaneman=findViewById(R.id.txtjnstaneman);
        tgltanem=findViewById(R.id.txttanggalpilihan);
        //prediksi
        prdkparalon=findViewById(R.id.txtprediksiparalon);
        prdkpanen=findViewById(R.id.txtpanen);
        prdkpenjualan=findViewById(R.id.txtestimasipenjualan);




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
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + dayOfMonth + "/" + month + "/" + year);

                String date = dayOfMonth + "/" +month + "/" + year;
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
        SimpleDateFormat fklndr = new SimpleDateFormat("dd/MM/yyyy");
        String keluaran = fklndr.format(klndr.getTime());
        tgltanem.setText(keluaran);




        //fungsi account
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        String j =(String) b.get("idnya");



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
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
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
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
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
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                k.setTime(sdf1.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            k.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

            String output = sdf1.format(k.getTime());
            //prediksi panen

            Calendar s = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
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

}