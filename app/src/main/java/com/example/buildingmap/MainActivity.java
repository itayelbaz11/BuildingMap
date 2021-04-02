package com.example.buildingmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * moves to pedometer
     * @param view
     */
    public void navigator(View view) {
        Intent si=new Intent(this,Main2Activity.class);
        startActivity(si);
    }


}
