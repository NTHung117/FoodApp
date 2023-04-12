package com.example.foodapp.retrofit;

import com.example.foodapp.model.NotiResponse;
import com.example.foodapp.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    //Mảng
                    "Content-Type: application/json",
                    "Authorization: key=AAAAXB2BMhU:APA91bGkE0083d12p3MnKTAa2eTE_zqvr8u40_SCiUZOG_AMBfqC2ssJzwOCCVryCdwfnn7EpYX7X0Zw_pcv0bY1jdWwR8hlSDAPms6E1zgvZCW06nFCpqQYO4pmYjqRn0qvcbqXRY_V"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data); //Notiresponse là được từ data trả về
}
