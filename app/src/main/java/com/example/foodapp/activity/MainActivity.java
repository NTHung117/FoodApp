package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.adapter.LoaiSpAdapter;
import com.example.foodapp.adapter.SanPhamMoiAdapter;
import com.example.foodapp.model.LoaiSp;
import com.example.foodapp.model.SanPhamMoi;
import com.example.foodapp.model.SanPhamMoiModel;
import com.example.foodapp.model.User;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        Paper.init(this);//Khởi tạo thư viện paper
        if (Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        AnhXa();
        ActionBar();

        if (isConnected(this)){

            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else {
            Toast.makeText(getApplicationContext(), "Không có kết nối internet, hãy thử lại", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent xemdonhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(xemdonhang);
                        break;
                    case 6:
                        //Xóa case user, để nó không tự động đăng nhập thẳng vào màn hình home
                        Paper.book().delete("user");
                        Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangxuat);
                        finish();
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerView.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Kết nối thất bại với Server"+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()){
                                mangloaisp = loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Đăng xuất", "https://cdn.icon-icons.com/icons2/2518/PNG/512/logout_icon_151219.png"));
                                //Khoi tao adapter
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }

    private void ActionViewFlipper() {
        List<String> hinhAnhQuangCao = new ArrayList<>();
        hinhAnhQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        hinhAnhQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        hinhAnhQuangCao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < hinhAnhQuangCao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(hinhAnhQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000); //Set thời gian bằng mili giây
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        //Set sự kiện click cho menu
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarHome);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationView);
        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);
        drawerLayout = findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.cart_sl);
        frameLayout = findViewById(R.id.frameGioHang);
        imgSearch = findViewById(R.id.imgSearch);
        //Khoi tao List
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Utils.manggiohang == null){ //Nếu như manggiohang rỗng thì nó sẽ khởi tạo
            Utils.manggiohang = new ArrayList<>();
        }else { //Nếu nó không null thì cho nó hiển thị lên và nếu người dùng bấm vào thêm vào giỏ hàng thì số lượng sẽ được cộng thêm
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoLuong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        //Giỏ Hàng
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gioHang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(gioHang);
            }
        });
        //Search
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoLuong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo moblie = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (moblie != null && moblie.isConnected()) ) {
            return true;
        }else {
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}