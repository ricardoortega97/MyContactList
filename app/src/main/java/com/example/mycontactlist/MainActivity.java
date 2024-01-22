package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiListButton();
    }
    private void intiListButton(){
        ImageButton ibList = findViewById(R.id.contactsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(intent);
            }
            private void startActivities(Intent intent) {
            }
        });
    }
    private void intiMapButton(){
        ImageButton ibList = findViewById(R.id.mapButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContactListActivity.class);//change this to maps
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(intent);
            }
            private void startActivities(Intent intent) {
            }
        });
    }
    private void intiSettingsButton(){
        ImageButton ibList = findViewById(R.id.settingsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ContactListActivity.class);//change this to settings
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivities(intent);
            }
            private void startActivities(Intent intent) {
            }
        });
    }
}