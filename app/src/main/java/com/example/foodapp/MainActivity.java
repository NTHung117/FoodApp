package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewManHinhChinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarHome);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recyclerView);
        navigationView = findViewById(R.id.navigationView);
        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);
    }
}