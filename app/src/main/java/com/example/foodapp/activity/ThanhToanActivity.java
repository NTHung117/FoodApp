package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.R;
import com.example.foodapp.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongTien, txtSodienthoai, txtEmail;
    EditText editDiaChi;
    AppCompatButton btnDatHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        initControl();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtSodienthoai = findViewById(R.id.txtSdt);
        txtEmail = findViewById(R.id.txtEmail);
        btnDatHang = findViewById(R.id.btnDatHang);
        editDiaChi = findViewById(R.id.editDiaChi);
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long tongTien = getIntent().getLongExtra("tongTien",0);
        txtTongTien.setText(decimalFormat.format(tongTien));
        txtEmail.setText(Utils.user_current.getEmail());
        txtSodienthoai.setText(Utils.user_current.getPhoneNumber());

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = editDiaChi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){//Người dùng để trống địa chỉ
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_LONG).show();
                }else {
                    //Post dữ liệu lên sever
                    //Để post toàn bộ giỏ hàng lên database t sẽ chuyển cái giỏ hàng thành một cái chuỗi json và sử dụng thư viện json để chuyển nó
                    Log.d("test", new Gson().toJson(Utils.manggiohang));
                }
            }
        });
    }
}