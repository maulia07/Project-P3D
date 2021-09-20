package com.mauliamahardika.cobalogindenganxampp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,MenuUtama.class);
                startActivity(i);
            }
        });

    }

    public void lupapassword(View view) {
        Intent a = new Intent(MainActivity.this,registrasi.class);
        startActivity(a);
    }
}