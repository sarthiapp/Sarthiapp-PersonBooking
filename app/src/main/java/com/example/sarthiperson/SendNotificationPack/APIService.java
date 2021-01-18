package com.example.sarthiperson.SendNotificationPack;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAACgtjh4w:APA91bHfy1BkoTXGDQ8ytAkqhSvFHRPnwFS2XCuBDD991PvzAvoU6cB9ZpR_wz6ESg5-9QE2FX_BMouRpwWVipxMlr1XwpcIwV-H58lJtjJp7FmNS6GC9knn-0BwfXthvXQhm86h0G2b"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
