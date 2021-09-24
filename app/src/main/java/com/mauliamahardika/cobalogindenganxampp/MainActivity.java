package com.mauliamahardika.cobalogindenganxampp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputEditText name, password;
    //private Button btn_login;
   // private TextView link_regist;
    //private ProgressBar loading;
    private static String URL_LOGIN = "http://192.168.0.3/hidroponik/login.php";
    SessionManager sessionManager;
    MaterialButton login;
    Button daftaracc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        name=findViewById(R.id.namalogin);
        password=findViewById(R.id.passwordlogin);
        login=findViewById(R.id.btnlogin);
        daftaracc=findViewById(R.id.daftarakun);

        //fungsi button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i=new Intent(MainActivity.this,IndexMenu.class);
                startActivity(i);*/
                String mName = name.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if (!mName.isEmpty() || !mPass.isEmpty()) {
                    Login(mName, mPass);
                } else {
                    name.setError("Tolong Masukan Nama");
                    password.setError("Tolong Masukan Password");
                }

            }
        });
        daftaracc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,registrasi.class);
                startActivity(i);
                finish();
            }
        });




    }

    //koneksi login
    private void Login(final String name, final String password) {

       // loading.setVisibility(View.VISIBLE);
        //
        // btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                   String name = object.getString("name").trim();
                                   // String email = object.getString("email").trim();
                                   String id = object.getString("id").trim();

                                    sessionManager.createSession(name,id);

                                    Intent intent = new Intent(MainActivity.this, IndexMenu.class);
                                    intent.putExtra("name", name);
                                    //intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(MainActivity.this,"Login Berhasil",Toast.LENGTH_LONG).show();

                                    //loading.setVisibility(View.GONE);


                                }

                            }else {
                                Toast.makeText(MainActivity.this,"Periksa password dan Nama Pengguna",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                         //   loading.setVisibility(View.GONE);
                            //  btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Error " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // loading.setVisibility(View.GONE);
                        //btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}