package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactID = contacts.get(position).getContactID();
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactID", contactID);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        intiListButton();
        intiMapButton();
        intiSettingsButton();
        //List activation
        ContactDataSource ds = new ContactDataSource(this);



        try {
            ds.open();
            contacts = ds.getContacts();
            ds.close();
            RecyclerView contactList = findViewById(R.id.rvContacts);
            //creates an instance of layoutManager to display a scrolling list
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            //message displays in the current activity
            contactList.setLayoutManager(layoutManager);
            ContactAdapter contactAdapter = new ContactAdapter(contacts);
            contactList.setAdapter(contactAdapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts ", Toast.LENGTH_LONG).show();
        }

    }


    private void intiListButton(){
        ImageButton ibList = findViewById(R.id.contactsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    private void intiMapButton(){
        ImageButton ibList = findViewById(R.id.mapButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,ContactMapActivity.class);//change this to maps
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    private void intiSettingsButton(){
        ImageButton ibList = findViewById(R.id.settingsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,ContactSettingsActivity.class);//change this to settings
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }


}