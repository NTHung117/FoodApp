package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodapp.R;

public class DangKyActivity extends AppCompatActivity {
    EditText email, pass, repass;
    AppCompatButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
    }

    private void initView() {
        email = findViewById(R.id.emailReg);
        pass = findViewById(R.id.passReg);
        repass = findViewById(R.id.repassReg);
        button = findViewById(R.id.btnDangKy);
    }
}