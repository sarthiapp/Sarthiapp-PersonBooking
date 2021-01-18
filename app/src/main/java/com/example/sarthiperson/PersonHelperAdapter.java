package com.example.sarthiperson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonHelperAdapter extends RecyclerView.Adapter {
    List<PersonFetchData> fetchDataList;

    public PersonHelperAdapter(List<PersonFetchData> fetchDataList) {
        this.fetchDataList = fetchDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_information,parent,false);
        ViewHolderClass viewHolderClass=new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        PersonFetchData personFetchData=fetchDataList.get(position);
        viewHolderClass.username.setText(personFetchData.getUserName());
        viewHolderClass.userAddress.setText(personFetchData.getUserAddress());
        viewHolderClass.userPhoneNumber.setText(personFetchData.getUserPhoneNumber().toString());



    }

    @Override
    public int getItemCount() {
        
            return fetchDataList.size();


    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView username,userAddress,userPhoneNumber;


        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.name);
            userAddress=itemView.findViewById(R.id.address);
            userPhoneNumber=itemView.findViewById(R.id.phonenumber);


        }
    }
}
