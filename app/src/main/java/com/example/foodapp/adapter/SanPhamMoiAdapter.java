package com.example.foodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Interface.ItemClickListener;
import com.example.foodapp.R;
import com.example.foodapp.activity.ChiTietActivity;
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
        holder.textTen.setText(sanPhamMoi.getTensanpham().trim());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); //Xử dụng thư viện decimalformat để xử lý dữ liệu số. Chúng ta định cứ 3 số sẽ phẩy một lần
        holder.textGia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasanpham()))+ "đ"); //Xử dụng parsedouble để chuyển đổi string thành double
        Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imageHinhAnh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick){
                    //Click
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTen, textGia;
        ImageView imageHinhAnh;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTen = itemView.findViewById(R.id.itemspmoi_ten);
            textGia = itemView.findViewById(R.id.itemspmoi_gia);
            imageHinhAnh = itemView.findViewById(R.id.itemspmoi_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
