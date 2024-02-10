package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        intiListButton();
        intiMapButton();
        intiSettingsButton();
        initGetLocationButton();

    }
    private void intiListButton(){
        ImageButton ibList = findViewById(R.id.contactsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this,ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    private void intiMapButton(){
        ImageButton ibList = findViewById(R.id.mapButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageButton ibSettings = findViewById(R.id.settingsButton);
                ibSettings.setEnabled(false);
            }
        });
    }
    private void intiSettingsButton(){
        ImageButton ibList = findViewById(R.id.settingsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactMapActivity.this,ContactSettingsActivity.class);//change this to settings
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(new Intent[]{intent});
            }
        });
    }
    //Get location button method
    private void initGetLocationButton() {
        Button locationButton = findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editAddress = (EditText) findViewById(R.id.addressInputMap);
                EditText editCity = (EditText) findViewById(R.id.editCity);
                EditText editSate = (EditText) findViewById(R.id.editState);
                EditText editZipcode = (EditText) findViewById(R.id.editZipcode);

                String address = editAddress.getText().toString() + ","  +
                        editCity.getText().toString() + ","  +
                        editSate.getText().toString() + ","  +
                        editZipcode.getText().toString();
                //create a list object that holds the address to declare
                List<Address> addresses = null;
                Geocoder geo = new Geocoder(ContactMapActivity.this);
                try { //try catch method to protect the app from errors
                    addresses = geo.getFromLocationName(address, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                TextView txtLatitude = findViewById(R.id.textLatitude);
                TextView txtLongitude = findViewById(R.id.textLongitude);

                txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                txtLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));
            }
        });
    }


}