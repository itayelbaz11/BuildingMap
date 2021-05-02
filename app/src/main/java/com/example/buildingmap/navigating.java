package com.example.buildingmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Stack;

public class navigating extends AppCompatActivity implements SensorEventListener {


    final int N=0;
    final int NE=1;
    final int E=2;
    final int SE=3;
    final int S=4;
    final int SW=5;
    final int W=6;
    final int NW=7;


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

    boolean stepsB=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigating);


        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(navigating.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        magnometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(navigating.this,magnometer,SensorManager.SENSOR_DELAY_NORMAL);

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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

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
        if(sensorEvent.sensor==mStepCounter){
            stepsB=true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void navigationU(Stack<Vector> path){
        Vector popV;
        while(!path.isEmpty()){
            while(path.peek().steps!=0){
                // לעשות גרפיקה של כמות צעדים וכיוון!!!!!!
                if(stepsB){
                    stepsB=false;
                    Vector peekV=path.peek();
                    int direction=directionGet(rotation);
                    if(direction==peekV.direction){
                        path.peek().steps--;
                    }
                    else{
                        if(direction==(peekV.direction+4)%8){
                            peekV.steps++;
                        }
                        else{
                            path.push(new Vector((direction+4)%8,1));
                        }
                    }
                }
            }
            popV=path.pop();
        }
        //הגעת ליעד!!!!!!!!
    }

    public int directionGet(double rotation){
        int direction=0;
    if(rotation>=22.5&&rotation<=67.5){
        direction=NE;
    }
    if(rotation>=112.5&&rotation<=157.5){
        direction=SE;
    }
    if(rotation>=202.5&&rotation<=247.5){
        direction=SW;
    }
    if(rotation>=292.5&&rotation<=337.5){
        direction=NW;
    }
    if(rotation>67.5&&rotation<112.5){
        direction=E;
    }
    if(rotation>157.5&&rotation<202.5){
       direction=S;
    }
    if(rotation>247.5&&rotation<292.5){
       direction=W;
    }
    if(rotation >337.5||rotation<22.5){
        direction=N;
  }
    return  direction;
}
}
