package com.mauliamahardika.cobalogindenganxampp.fiturrecyclemasatanam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mauliamahardika.cobalogindenganxampp.R;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {

    //Context mCtx;
    List<Produk>produkList;

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
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView aNamapenanam,aJenistanaman,aJumlahtanaman,aTglSemai,aPindahParalon,aPanen,aEstimasiPenjualan;


        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
           aNamapenanam=itemView.findViewById(R.id.Tnamapenanam);
            aJenistanaman=itemView.findViewById(R.id.Tjenistanaman);
            aJumlahtanaman=itemView.findViewById(R.id.Tjumlahtanaman);
            aTglSemai=itemView.findViewById(R.id.Ttglsemai);
            aPindahParalon=itemView.findViewById(R.id.Tpindahparalon);
            aPanen=itemView.findViewById(R.id.Ttglpanen);
            aEstimasiPenjualan=itemView.findViewById(R.id.Testimasipenjualan);


        }
    }
}
