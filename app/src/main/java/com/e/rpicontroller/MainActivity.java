package com.e.rpicontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        JoyStickView joystick = new JoyStickView(this);
        setContentView(R.layout.activity_main);
    }
}
