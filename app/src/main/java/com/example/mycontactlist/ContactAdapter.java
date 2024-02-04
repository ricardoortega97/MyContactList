package com.example.mycontactlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter  {
    private ArrayList<String> contactData;
//Sets a code behavior of the viewHolder
    private View.OnClickListener mOnItemClickerListener;
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;
        //delcare the constructor method for viewHolder
        public ContactViewHolder(@NonNull View itemView) {//cannot return a null value
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textViewName);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickerListener);
        }
        //Decalres a method to return the TextView layout
        public TextView getContactTextView() {
            return textViewContact;
        }
    }//constructor method to decalre the adapter and associate data to be displayed
    public ContactAdapter(ArrayList<String> arrayList) {
        contactData = arrayList;
    }
    public  void setOnItemClickerListener(View.OnClickListener itemClickListener) {
        mOnItemClickerListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view,
               parent, false);
       return new ContactViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position));
    }
    @Override
    public int getItemCount() {
        return contactData.size();
    }


}
