package com.example.sarthiperson.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sarthiperson.PersonActivity;
import com.example.sarthiperson.R;
import com.example.sarthiperson.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.example.sarthiperson.MyAdapter.context;

public class MyFireBaseMessagingService  extends FirebaseMessagingService {
    String title,message;
     String add;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");

        Intent intent= new Intent(context, PersonActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        FirebaseDatabase.getInstance().getReference().child("USERLOCATION").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                add= Objects.requireNonNull(dataSnapshot).getValue(String.class);

                Log.d("address:",add);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        intent.putExtra("Add",add);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String CHANNEL_ID="MESSAGE";
        String CHANNEL_NAME="MESSAGE";
        NotificationManagerCompat manager= NotificationManagerCompat.from(getApplicationContext());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(getRandomNumber(),notification.build());

    }
    private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
    }
}




