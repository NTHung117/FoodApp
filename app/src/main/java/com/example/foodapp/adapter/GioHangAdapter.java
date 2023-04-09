package com.example.foodapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Interface.ImageClickListener;
import com.example.foodapp.R;
import com.example.foodapp.model.EventBus.TinhTongEvent;
import com.example.foodapp.model.GioHang;
import com.example.foodapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

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
        holder.item_giohang_giaSp.setText(decimalFormat.format((gioHang.getGiaSp())));
        long gia = gioHang.getSoLuong() * gioHang.getGiaSp();
        holder.item_giohang_tongTien.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Utils.mangmuahang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent()); //Gửi sự kiện qua model TinhTongEvent
                }else {
                    for (int i = 0; i < Utils.mangmuahang.size(); i++){//Khi t click bỏ chọn, t phải chui vào mảng mua hàng, t duyệt qua tất cả phẩn tử của mảng mua hàng,
                         //t tìm xem có id nào chung với id vừa click không để chúng ta bỏ nó đi
                        if (Utils.mangmuahang.get(i).getIdSp() == gioHang.getIdSp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent()); //Gửi sự kiện qua model TinhTongEvent
                        }
                    }
                }
            }
        });
        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1){
                    if (gioHangList.get(pos).getSoLuong() < 11){ //Set số lượng không được quá 11
                        int soluongMoi = gioHangList.get(pos).getSoLuong() + 1;// Số lương mới = số lượng cũ + 1
                        gioHangList.get(pos).setSoLuong(soluongMoi);//Sau khi cộng xong thì t sẽ set số lượng mới
                    }
                    holder.item_giohang_soLuong.setText(gioHangList.get(pos).getSoLuong() + " ");
                    long gia = gioHangList.get(pos).getSoLuong() * gioHangList.get(pos).getGiaSp();
                    holder.item_giohang_tongTien.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent()); //Gửi sự kiện qua model TinhTongEvent
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoLuong() > 1){//Kiểm tra số lượng sản phẩm trong giỏ hàng có lớn hơn 1 không, Nếu lớn hơn 1 thì mới cho trừ
                        int soluongMoi = gioHangList.get(pos).getSoLuong() - 1;// Số lương mới = số lượng cũ - 1
                        gioHangList.get(pos).setSoLuong(soluongMoi);//Sau khi trừ xong thì t sẽ set số lượng mới
                        holder.item_giohang_soLuong.setText(gioHangList.get(pos).getSoLuong() + " ");
                        long gia = gioHangList.get(pos).getSoLuong() * gioHangList.get(pos).getGiaSp();
                        holder.item_giohang_tongTien.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent()); //Gửi sự kiện qua model TinhTongEvent
                    } else if (gioHangList.get(pos).getSoLuong() == 1) { //Trong trường hợp nó bằng 1 và bấm dấu trừ thì nó sẽ xóa ra khỏi giỏ hàng
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Bạn có chắc muốn xóa sản phẩm khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {//Nếu người dùng bấm đồng ý thì xóa spham ra khỏi giỏ hàng
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Xóa sản phẩm đó ra khỏi giỏ hàng
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();//Cập nhập lại dữ liệu
                                EventBus.getDefault().postSticky(new TinhTongEvent()); //Gửi sự kiện qua model TinhTongEvent
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_img, img_giohang_cong, img_giohang_tru;
        TextView item_giohang_tenSp, item_giohang_giaSp, item_giohang_soLuong, item_giohang_tongTien;
        ImageClickListener listener;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_img = itemView.findViewById(R.id.item_giohang_img);
            item_giohang_tenSp = itemView.findViewById(R.id.item_giohang_tenSp);
            item_giohang_giaSp = itemView.findViewById(R.id.item_giohang_giaSp);
            item_giohang_soLuong = itemView.findViewById(R.id.item_giohang_soLuong);
            item_giohang_tongTien = itemView.findViewById(R.id.item_giohang_tongTien);
            img_giohang_cong =  itemView.findViewById(R.id.item_giohang_cong);
            img_giohang_tru =  itemView.findViewById(R.id.item_giohang_tru);
            checkBox = itemView.findViewById(R.id.item_giohang_checkbox);

            //event click
            img_giohang_cong.setOnClickListener(this);
            img_giohang_tru.setOnClickListener(this);
        }

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view == img_giohang_cong){
                listener.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == img_giohang_tru) {
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
