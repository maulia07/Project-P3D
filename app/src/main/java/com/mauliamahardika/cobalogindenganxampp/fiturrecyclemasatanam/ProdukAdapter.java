package com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mauliamahardika.cobalogindenganxampp.IndexMenu;
import com.mauliamahardika.cobalogindenganxampp.R;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.MasaTanam;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.Nutrisi;
import com.mauliamahardika.cobalogindenganxampp.kontenmonitoring.Tentang;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {
    private static final String URL_HAPUS = "http://192.168.0.3/hidroponik/hapusmasatanam.php";
    //Context mCtx;
    List<Produk>produkList;

    //private final View.OnClickListener mOnClickListener=new myOnclickListener();

    public ProdukAdapter(Context mCtx,List<Produk> produkList){
        //this.mCtx=mCtx;
        this.produkList=produkList;
    }


    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //  LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.listprediksi, null);
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.listprediksi,null);
       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produk produk=produkList.get(viewType);



                Toast.makeText(view.getContext(),"Pesannya "+g,Toast.LENGTH_LONG).show();


            }
        });


        /*View view=LayoutInflater.inflate(R.layout.listprediksi,parent,false);
        ProdukViewHolder produkViewHolder=new ProdukViewHolder(view);*/

        return new  ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        Produk produk=produkList.get(position);
        holder.aNamapenanam.setText(produk.getNamapenanam());
        holder.aJenistanaman.setText(produk.getJenistanaman());
        holder.aJumlahtanaman.setText(produk.getJumlahtanaman());
        holder.aTglSemai.setText(produk.getTglsemai());
        holder.aPindahParalon.setText(produk.getPrediksiparalon());
        holder.aPanen.setText(produk.getPrediksipanen());
        holder.aEstimasiPenjualan.setText(produk.getEstimasipenjualan());

        //Jika Prediksi Paralon
        //tanggalan ambil dari xampp paralon
        String preParalon=produk.getPrediksiparalon();
        String[] valuespral = preParalon.split("-");
        String daypral = String.valueOf(valuespral[0]);
        String monthpral = String.valueOf(valuespral[1]);
        String yearpral = String.valueOf(valuespral[2]);
        int h=Integer.parseInt(daypral+monthpral+yearpral);
        //tanggalan sekarang
        //Calendar s = Calendar.getInstance();
        Date outpral = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String output2 = sdf2.format(outpral.getTime());
        String[] valuesoutpral = output2.split("-");
        String dayoutpral = String.valueOf(valuesoutpral[0]);
        String monthoutpral = String.valueOf(valuesoutpral[1]);
        String yearoutpral = String.valueOf(valuesoutpral[2]);
        int g=Integer.parseInt(dayoutpral+monthoutpral+yearoutpral);
        if (g>=h){
            holder.imgpral.setImageResource(R.drawable.ic_baseline_check_circle_24_ijo);
        }else {
            holder.imgpral.setImageResource(R.drawable.ic_baseline_check_circle_24_abuabu);

        }


        //Jika Prediksi Panen
        //tanggalan ambil dari xampp paralon
        String prePanen=produk.getPrediksipanen();
        String[] valuespanen = prePanen.split("-");
        String daypanen = String.valueOf(valuespanen[0]);
        String monthpanen = String.valueOf(valuespanen[1]);
        String yearpanen = String.valueOf(valuespanen[2]);
        int panen=Integer.parseInt(daypanen+monthpanen+yearpanen);
        if (g>=panen){
            holder.vpanen.setImageResource(R.drawable.ic_baseline_check_circle_24_ijo);
        }else {
            holder.vpanen.setImageResource(R.drawable.ic_baseline_check_circle_24_abuabu);

        }

        //Jika Semai
        //Tanggalan Ambil dari semai
        String preSemai=produk.getTglsemai();
        String[] valueSemai = preSemai.split("-");
        String daySemai = String.valueOf(valueSemai[0]);
        String monthSemai = String.valueOf(valueSemai[1]);
        String yearSemai = String.valueOf(valueSemai[2]);
        int semai=Integer.parseInt(daySemai+monthSemai+yearSemai);
        if (g>=semai){
            holder.vsemai.setImageResource(R.drawable.ic_baseline_check_circle_24_ijo);
        }else {
            holder.vsemai.setImageResource(R.drawable.ic_baseline_check_circle_24_abuabu);

        }

        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a= String.valueOf(produk.getSeri());

                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Hapus Prediksi");
                builder.setMessage("Apakah Anda Yakin Ingin Menghapus ?"+produk.getNamapenanam());
                builder.setNegativeButton("Ga Jadi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HAPUS,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                      //  progressDialog.dismiss();

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");


                                            if (success.equals("1")){
                                                //Toast.makeText(Nutrisi.this, "Berhasil Di Tambahkan!", Toast.LENGTH_SHORT).show();
                                                //sessionManager.createSession(name, email, id);
                                                produkList.remove(position);
                                                notifyDataSetChanged();
                                                notifyItemChanged(position);
                                                notifyItemRangeChanged(position,produkList.size());
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                         //   progressDialog.dismiss();
                                            //  Toast.makeText(Nutrisi.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //progressDialog.dismiss();
                                        //Toast.makeText(Nutrisi.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("seri", a);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                        requestQueue.add(stringRequest);

                    }
                });
AlertDialog dialog=builder.create();
dialog.show();

                //Toast.makeText(v.getContext(),"Pesan"+a,Toast.LENGTH_LONG).show();






            }
        });


    }



    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView aNamapenanam,aJenistanaman,aJumlahtanaman,aTglSemai,aPindahParalon,aPanen,aEstimasiPenjualan;
        ImageView delet,imgpral,vpanen,vsemai;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
           aNamapenanam=itemView.findViewById(R.id.Tnamapenanam);
            aJenistanaman=itemView.findViewById(R.id.Tjenistanaman);
            aJumlahtanaman=itemView.findViewById(R.id.Tjumlahtanaman);
            aTglSemai=itemView.findViewById(R.id.Ttglsemai);
            aPindahParalon=itemView.findViewById(R.id.Tpindahparalon);
            aPanen=itemView.findViewById(R.id.Ttglpanen);
            aEstimasiPenjualan=itemView.findViewById(R.id.Testimasipenjualan);
            delet=itemView.findViewById(R.id.hapus);
            imgpral=itemView.findViewById(R.id.checkPral);
            vpanen=itemView.findViewById(R.id.imgpanen);
            vsemai=itemView.findViewById(R.id.imgsemai);

        }
    }


}
