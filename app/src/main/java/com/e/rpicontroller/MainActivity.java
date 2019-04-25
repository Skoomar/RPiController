package com.e.rpicontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity { //implements JoystickView.JoystickListener {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_main);
    }

    /*
    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch(id) {
            case R.id.joystickLeft:
                Log.d("Left Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                break;
            //case R.id.joystickRight:
                //Log.d("Right Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
        }
        //Log.d("Main Method", "X percent: " + xPercent + "Y percent: " + yPercent);
    }*/
}
