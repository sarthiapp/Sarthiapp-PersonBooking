package com.example.sarthiperson;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.sarthiperson.SendNotificationPack.MyFireBaseMessagingService;
import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonActivity extends AppCompatActivity {
    public static  final String TAG="MyTag";
    protected TextView Addresstxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Addresstxt=(TextView)findViewById(R.id.text1) ;
        String msg=getIntent().getExtras().getString("Add");
        Log.d("ddd", msg);
        Addresstxt.setText(msg);
      /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful()){

                    String token=task.getResult().getToken();
                    Log.d(TAG,"TOKEN:"+token);
                    FirebaseDatabase.getInstance().getReference("Tokens").child(Name).child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
*/


     


}



private static int getRandomNumber() {
        Date dd= new Date();
        SimpleDateFormat ft =new SimpleDateFormat ("mmssSS");
        String s=ft.format(dd);
        return Integer.parseInt(s);
        }
}