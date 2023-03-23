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
import com.example.foodapp.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.textTen.setText(sanPhamMoi.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); //Xử dụng thư viện decimalformat để xử lý dữ liệu số. Chúng ta định cứ 3 số sẽ phẩy một lần
        holder.textGia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasanpham()))+ "đ"); //Xử dụng parsedouble để chuyển đổi string thành double
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imageHinhAnh);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTen, textGia;
        ImageView imageHinhAnh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTen = itemView.findViewById(R.id.itemspmoi_ten);
            textGia = itemView.findViewById(R.id.itemspmoi_gia);
            imageHinhAnh = itemView.findViewById(R.id.itemspmoi_image);
        }
    }
}
