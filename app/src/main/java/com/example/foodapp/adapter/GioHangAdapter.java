package com.example.foodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.model.GioHang;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tenSp.setText(gioHang.getTenSp());
        holder.item_giohang_soLuong.setText(gioHang.getSoLuong() + " "); //Số lượng đang là kiểu int nên t ép nó về kiểu chuỗi
        Glide.with(context).load(gioHang.getHinhSp()).into(holder.item_giohang_img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_giaSp.setText(decimalFormat.format((gioHang.getGiaSp()))+ "đ");
        long gia = gioHang.getSoLuong() * gioHang.getGiaSp();
        holder.item_giohang_tongTien.setText(decimalFormat.format(gia));
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView item_giohang_img;
        TextView item_giohang_tenSp, item_giohang_giaSp, item_giohang_soLuong, item_giohang_tongTien;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_img = itemView.findViewById(R.id.item_giohang_img);
            item_giohang_tenSp = itemView.findViewById(R.id.item_giohang_tenSp);
            item_giohang_giaSp = itemView.findViewById(R.id.item_giohang_giaSp);
            item_giohang_soLuong = itemView.findViewById(R.id.item_giohang_soLuong);
            item_giohang_tongTien = itemView.findViewById(R.id.item_giohang_tongTien);
        }
    }
}
