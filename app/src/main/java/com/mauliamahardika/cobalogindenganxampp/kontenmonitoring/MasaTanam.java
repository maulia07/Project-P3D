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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.masatanam.IsiMasaTanam;

public class MasaTanam extends AppCompatActivity {
    FloatingActionButton addnya;
    TextView t;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masa_tanam);

        addnya=findViewById(R.id.btnadd);
        t=findViewById(R.id.tanam);
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
        t.setText(j);

        addnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MasaTanam.this, IsiMasaTanam.class);
                i.putExtra("idnya",j);
                startActivity(i);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        //  moveTaskToBack(true);

        Intent i=new Intent(MasaTanam.this,IndexMenu.class);
        startActivity(i);
        finish();
    }
}