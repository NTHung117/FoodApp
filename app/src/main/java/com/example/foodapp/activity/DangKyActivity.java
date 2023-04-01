package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapp.R;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;

import org.greenrobot.eventbus.android.AndroidLogger;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, pass, repass, phoneNumber, userName;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_phoneNumber = phoneNumber.getText().toString().trim();
        String str_userName = userName.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập lại mật khẩu", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(str_userName)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập tên của bạn", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(str_phoneNumber)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_LONG).show();
        }else {
            if (str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangKy(str_email,str_pass,str_userName,str_phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(), "Thành Công", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập lại đúng mật khẩu", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.emailReg);
        pass = findViewById(R.id.passReg);
        repass = findViewById(R.id.repassReg);
        phoneNumber = findViewById(R.id.phonenumberReg);
        button = findViewById(R.id.btnDangKy);
        userName = findViewById(R.id.userReg);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}