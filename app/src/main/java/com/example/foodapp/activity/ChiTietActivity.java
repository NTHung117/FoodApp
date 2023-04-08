package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.model.GioHang;
import com.example.foodapp.model.SanPhamMoi;
import com.example.foodapp.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnThemVaoGioHang;
    ImageView imageHinhAnh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    FrameLayout frameLayoutGioHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            //Kiểm tra xem có bị trùng sản phẩm không, t duyệt qua từng phần tử của mảng
            for (int i = 0 ; i < Utils.manggiohang.size(); i++){
                if (Utils.manggiohang.get(i).getIdSp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoLuong(soLuong + Utils.manggiohang.get(i).getSoLuong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasanpham());
                    Utils.manggiohang.get(i).setGiaSp(gia);
                    flag = true;//Nếu sản phẩm đã trùng thì cho nó bằng true
                }
            }
            if (flag == false){//Nếu nó không trùng thì chúng ta lấy nó như bình thường
                long gia = Long.parseLong(sanPhamMoi.getGiasanpham());
                GioHang gioHang = new GioHang();
                gioHang.setIdSp(sanPhamMoi.getId());
                gioHang.setTenSp(sanPhamMoi.getTensanpham());
                gioHang.setHinhSp(sanPhamMoi.getHinhanh());
                gioHang.setGiaSp(gia);
                gioHang.setSoLuong(soLuong);
                Utils.manggiohang.add(gioHang);
            }
        }else {
            int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasanpham());
            GioHang gioHang = new GioHang();
            gioHang.setIdSp(sanPhamMoi.getId());
            gioHang.setTenSp(sanPhamMoi.getTensanpham());
            gioHang.setHinhSp(sanPhamMoi.getHinhanh());
            gioHang.setGiaSp(gia);
            gioHang.setSoLuong(soLuong);
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoLuong();
        }
        badge.setText(String.valueOf(totalItem));

    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensanpham());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imageHinhAnh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasanpham()))+ "đ");
        //Tạo một mảng để chứa dữ liệu spinner
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapterspin);

    }

    private void initView() {
        tensp = findViewById(R.id.txtTenSp);
        giasp = findViewById(R.id.txtGiaSp);
        mota = findViewById(R.id.txtMoTaChiTiet);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        spinner = findViewById(R.id.spinner);
        imageHinhAnh = findViewById(R.id.imgChiTiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.cart_sl);
        frameLayoutGioHang = findViewById(R.id.frameGioHang);
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}