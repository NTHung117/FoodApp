package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.adapter.GioHangAdapter;
import com.example.foodapp.model.EventBus.TinhTongEvent;
import com.example.foodapp.model.GioHang;
import com.example.foodapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView gioHangTrong, tongTien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnThanhToan;
    GioHangAdapter gioHangAdapter;
    long tongtienSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        if (Utils.mangmuahang != null){
            Utils.mangmuahang.clear();
        }
        tinhHoaDon();
    }

    private void tinhHoaDon() {
        tongtienSp = 0 ;
        for (int i = 0; i < Utils.mangmuahang.size(); i++){//Tính tiền trong mảng mua hàng chứ không tính tiền trong mảng giỏ hàng
            tongtienSp = tongtienSp + (Utils.mangmuahang.get(i).getGiaSp() * Utils.mangmuahang.get(i).getSoLuong());

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongTien.setText(decimalFormat.format(tongtienSp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.manggiohang.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongTien", tongtienSp);
                //Sau khi mua xong t clear giỏ hàng
                Utils.manggiohang.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        gioHangTrong = findViewById(R.id.txtGioHangTrong);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_gioHang);
        tongTien = findViewById(R.id.txtTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    //Sau khi đăng ký xong sự kiện EventBus trong hàm onStart thì t sẽ bắt sự kiện do bên kia gửi qua
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if (event != null){//Bên kia sẽ gửi qua một sự kiện, sau đó chúng ta kiểm tra nó khác null thì chúng ta sẽ tính lại
            tinhHoaDon();
        }
    }
}