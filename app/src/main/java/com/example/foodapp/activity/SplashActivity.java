package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodapp.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);//Khởi tạo thư viện paper
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3500);
                }catch (Exception ex){

                }finally {
                    if (Paper.book().read("user") == null){
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                        //Chuyển màn hình xong t finish luôn, chứ bấm back là nó vẫn quay lại
                        finish();
                    }else {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        //Chuyển màn hình xong t finish luôn, chứ bấm back là nó vẫn quay lại
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}