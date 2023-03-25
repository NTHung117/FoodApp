package com.example.foodapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.foodapp.R;
import com.example.foodapp.adapter.DienThoaiAdapter;
import com.example.foodapp.model.SanPhamMoi;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable(); //Sử dụng compositeDisposable của thư viện rxJava để nhận các phần tử từ Observerable và quản lý tài nguyên
    int page = 1;
    int loai;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        AnhXa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size()-1){ //Cho nó tìm đến phần tử cuối cùng của recycleView
                        isLoading = true; //Cho isLoading = true để cho nó không load nữa
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                sanPhamMoiList.add(null);
                adapterDt.notifyItemInserted(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Remove null
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterDt.notifyItemRemoved(sanPhamMoiList.size());
                page = page+1;
                getData(page);// Sau khi chúng ta get dữ liệu từ trang mới phải set nó về false lại để nó tiếp tục load
                adapterDt.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                if (adapterDt == null){
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    adapterDt = new DienThoaiAdapter(getApplicationContext(), sanPhamMoiList);
                                    recyclerView.setAdapter(adapterDt);
                                }else {
                                    int vitri = sanPhamMoiList.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i = 0 ; i < soluongadd; i++){
                                        sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                                        //Sau khi có dữ liệu rồi t duyệt qua cái dữ liệu mới lấy về trong sanPhamMoiModel rồi t add nó vào sanPhamMoiList rồi sau đó t thông báo cho adapterDt chúng ta add từ vị trí nào và số lượng bao nhiêu
                                    }
                                    adapterDt.notifyItemRangeInserted(vitri,soluongadd);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu rồi ba", Toast.LENGTH_LONG).show();
                                isLoading = true; //set isloading = true để nó không chạy thêm hàm loadmore
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Kết nối thất bại với server", Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Tạo sự kiện cho nút back trên Toolbar trong trang loại điện thoại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView_dt);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}