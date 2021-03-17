package com.example.buildingmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permissions;
import java.util.Stack;

public class Navigator extends AppCompatActivity  implements SensorEventListener{

    public static final int PERMISSIONS_FINE_LOCATION=99;
    /**
     * references to UI objects
     */
    TextView tv,tv2,tv3,X,Y,Z;

    int[][] m=new int[1000][1000];
    int x1=500,y1=500;
    int steps=0;

    boolean stst=false;

    ImageView imageView;


    /**
     * Google's API for location services.
     */
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    private SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnometer;

   String x,y,z;

    Bitmap bitmap,bmp;

    Sensor mStepCounter;
    boolean isCounterSensorPresent;
    int stepCount=0;

    float[] mGravity,mGeomagnetic;
    float azimuth,rotation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        /**
         * give each UI variable a value
         */




        /**
         * Set all properties of locationRequest.
         */


        imageView = (ImageView) findViewById(R.id.imageView);


        locationRequest=new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(100);


        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Navigator.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(Navigator.this,magnometer,SensorManager.SENSOR_DELAY_NORMAL);

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

    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }



    /**
     * a button that starts the location finding in this moment.
     */
    public void start(View view) {
       oM();
       stst=true;

    }

    public void oM(){
        bitmap=createImage(1000,1000, Color.WHITE);
        x1=500;
        y1=500;
        bitmap.setPixel(x1,y1,Color.BLACK);

    }




    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType()==Sensor.TYPE_STEP_COUNTER){

        if(sensorEvent.sensor==mStepCounter){
            steps++;
            if(steps==3){
                steps=0;
                if(rotation>=22.5&&rotation<=67.5){
                   for(int i=0;i<2;i++){
                       x1++;
                       y1++;
                       bitmap.setPixel(x1,y1,Color.BLACK);
                   }
                }
                if(rotation>=112.5&&rotation<=157.5){
                    for(int i=0;i<2;i++){
                        x1++;
                        y1--;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                if(rotation>=202.5&&rotation<=247.5){
                    for(int i=0;i<2;i++){
                        x1--;
                        y1--;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                if(rotation>=292.5&&rotation<=337.5){
                    for(int i=0;i<2;i++){
                        x1--;
                        y1++;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                if(rotation>67.5&&rotation<112.5){
                    for(int i=0;i<3;i++){
                        x1++;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }

                if(rotation>157.5&&rotation<202.5){
                    for(int i=0;i<3;i++){
                        y1--;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                if(rotation>247.5&&rotation<292.5){
                    for(int i=0;i<3;i++){
                        x1--;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                if(rotation >337.5||rotation<22.5){
                    for(int i=0;i<3;i++){
                        y1++;
                        bitmap.setPixel(x1,y1,Color.BLACK);
                    }
                }
                }

        }}
        if(sensorEvent.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
            x=String.valueOf(sensorEvent.values[0]);
            y=String.valueOf(sensorEvent.values[1]);
            z=String.valueOf(sensorEvent.values[2]);

        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = sensorEvent.values;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = sensorEvent.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimuth = orientation[0];
                rotation = (float) (( Math.toDegrees( azimuth ) + 360 ) % 360);
            }
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


    public void Stop(View view) {
        bmp=Bitmap.createBitmap(bitmap);

    }

    public void pic(View view) {
        imageView.setImageBitmap(bmp);
    }



}

