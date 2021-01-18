package com.example.sarthiperson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sarthiperson.SendNotificationPack.APIService;
import com.example.sarthiperson.SendNotificationPack.Client;
import com.example.sarthiperson.SendNotificationPack.Data;
import com.example.sarthiperson.SendNotificationPack.MyResponse;
import com.example.sarthiperson.SendNotificationPack.NotificationSender;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAdapter extends FirebaseRecyclerAdapter<FetchData,MyAdapter.MyViewHolder> implements LocationListener,ActivityCompat.OnRequestPermissionsResultCallback {
public static Context context;
    LocationManager locationManager;
    APIService apiService;
    public static String Name;
    public static String Address;
    public MyAdapter(@NonNull FirebaseRecyclerOptions<FetchData> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull FetchData model) {

        holder.name.setText(model.getName());
        Name= (String) holder.name.getText();
        holder.rating.setText("Rating :  "+Integer.toString(model.getRating()));
        holder.age.setText("Age :      "+Integer.toString(model.getAge()));
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else {
                    detectcurrentlocation();
                    FirebaseDatabase.getInstance().getReference().child("Tokens").child((String) holder.name.getText()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String usertoken=dataSnapshot.getValue(String.class);
                            sendNotifications(usertoken, "Booking","new booking");
                            Log.d("token:",usertoken);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    Toast.makeText(context, "you click on "+holder.name.getText(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder myDialogue = new AlertDialog.Builder(context);
                    myDialogue.setTitle("Do you want to book this person");
                    myDialogue.setPositiveButton("Book", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "you click on book", Toast.LENGTH_SHORT).show();
                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Booking...");

                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(10000);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                }
                            }).start();
                        }
                    });
                    myDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                        }
                    });
                    myDialogue.setCancelable(false);
                    //myDialogue.show();
                }
            }

        });
        holder.upadte_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "you click on rate", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder myDialogue=new AlertDialog.Builder(context);
                myDialogue.setTitle("Rate the person");
                final EditText rate=new EditText(context);
                rate.setInputType(InputType.TYPE_CLASS_NUMBER);
                myDialogue.setView(rate);
                myDialogue.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int d=Integer.parseInt(rate.getText().toString());
                        if(d<=5) {

                            Map<String, Object> map = new HashMap<>();
                            map.put("rating", d);
                            FirebaseDatabase.getInstance().getReference().child("persons").child(getRef(position).getKey())
                                    .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "your data is successfully updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(context, "Please provide the rating less than 5", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                myDialogue.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialogue.setCancelable(false);
                myDialogue.show();
            }
        });



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view);
    }
    @SuppressLint("MissingPermission")
    private void detectcurrentlocation() {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {
       // Locationhelper helper=new Locationhelper(location.getLatitude(),location.getLongitude());
        Log.d("loc",Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude()));
        locationManager.removeUpdates(this);
        try{
            Geocoder geocoder=new Geocoder(context, Locale.getDefault());
            List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Address=address.get(0).getAddressLine(0);

            FirebaseDatabase.getInstance().getReference("USERLOCATION").setValue(Address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                detectcurrentlocation();
            }
            else{
                Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {

                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d("Fail:", t.toString());
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,rating;
        CircleImageView img;
        public  Button btn_book,upadte_rate;
        TextView age;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            rating=itemView.findViewById(R.id.rating);
            img=itemView.findViewById(R.id.img1);
            btn_book=itemView.findViewById(R.id.btn_book);
            upadte_rate=itemView.findViewById(R.id.rate_update);
            age=itemView.findViewById(R.id.age);


        }
    }
}