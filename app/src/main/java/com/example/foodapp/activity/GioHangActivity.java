package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.adapter.GioHangAdapter;
import com.example.foodapp.model.GioHang;
import com.example.foodapp.utils.Utils;

import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView gioHangTrong, tongTien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnThanhToan;
    GioHangAdapter gioHangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
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
    }

    private void initView() {
        gioHangTrong = findViewById(R.id.txtGioHangTrong);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_gioHang);
        tongTien = findViewById(R.id.txtTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
    }
}