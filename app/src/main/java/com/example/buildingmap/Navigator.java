package com.example.buildingmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permissions;

public class Navigator extends AppCompatActivity  implements SensorEventListener{

    public static final int PERMISSIONS_FINE_LOCATION=99;
    /**
     * references to UI objects
     */
    TextView tv,tv2,tv3,X,Y,Z;

    int[][] m=new int[1000][1000];
    int x1=500,y1=500;


    /**
     * Google's API for location services.
     */
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    private SensorManager sensorManager;
    Sensor accelerometer;

    String x,y,z;

    Sensor mStepCounter;
    boolean isCounterSensorPresent;
    int stepCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        /**
         * give each UI variable a value
         */



        X=(TextView) findViewById(R.id.x);
        Y=(TextView) findViewById(R.id.y);
        Z=(TextView) findViewById(R.id.z);
        tv=(TextView) findViewById(R.id.tv);

        /**
         * Set all properties of locationRequest.
         */

        for(int i=0;i<1000;i++) {
            for(int j=0;j<1000;j++) {
                m[i][j]=0;

            }

        }

        locationRequest=new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(100);


        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Navigator.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            mStepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent=true;
        }
        else{
            isCounterSensorPresent=false;
        }

    }



    /**
     * a button that starts the location finding in this moment.
     */
    public void btn(View view) {
        X.setText("X:"+x);
        Y.setText("Y:"+y);
        Z.setText("Z:"+z);
        stepCount=0;
        tv.setText(""+stepCount);

    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_STEP_COUNTER){

        if(sensorEvent.sensor==mStepCounter){
            stepCount++;
            tv.setText(String.valueOf(stepCount));
        }}
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            x=String.valueOf(sensorEvent.values[0]);
            y=String.valueOf(sensorEvent.values[1]);
            z=String.valueOf(sensorEvent.values[2]);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this,mStepCounter);
        }
    }

    /**
     * COUNTING
     * @param view
     */
    public void start(View view) {
        stepCount=0;
        tv.setText(""+stepCount);
    }
}
