package com.e.rpicontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centreX, centreY;
    private float baseRadius, hatRadius;
    private boolean joystickHeld;
    private JoystickListener joystickCallBack;
    private float hypotenuse, sin, cos;
    private final int ratio = 5; // make this smaller for more shading


    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallBack = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    private void setupDimensions() {
        centreX = getWidth() / 2;
        centreY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight() / 4);
        hatRadius = Math.min(getWidth(), getHeight() / 7);
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {// prevents drawing if SurfaceView hasn't been created on screen, preventing runtime errors
            Canvas myCanvas = this.getHolder().lockCanvas(); // create canvas object
            Paint colours = new Paint();

            myCanvas.drawARGB(255, 255, 255, 255);
            //myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // clear background

            // draw base border
            colours.setStyle(Paint.Style.STROKE); // set brush to just draw line (unfilled shape)
            colours.setStrokeWidth(10);
            colours.setARGB(255, 0, 0, 0);
            myCanvas.drawCircle(centreX, centreY, baseRadius, colours);

            // draw base fill
            colours.setStyle(Paint.Style.FILL);
            colours.setARGB(255, 224, 224, 224); // set brush to joystick base colours
            myCanvas.drawCircle(centreX, centreY, baseRadius, colours); // draw joystick base

            //colours.setARGB(255, 100, 100, 100); // set brush to joystick top colours
            //myCanvas.drawCircle(newX, newY, hatRadius,colours);

            /*
            // to find the angle of the touched point relative to the centre of joystick
            hypotenuse = (float) Math.sqrt(Math.pow(newX - centreX, 2) + Math.pow(newY - centreY, 2));
            sin = (newY - centreY) / hypotenuse; // S = O/H
            cos = (newX - centreX) / hypotenuse; // C = A/H

            for (int i = 1; i < (int) baseRadius; i++) {
                //colours.setARGB(255/i, 224, 224, 224); // gradually decrease shade through loop
                colours.setARGB(255, 224, 224 , 224 );

                // gradually increase area that is shaded
                myCanvas.drawCircle(newX - cos * hypotenuse * (ratio / baseRadius) * i,
                        newY - sin * hypotenuse * (ratio / baseRadius) * i, i * (hatRadius * ratio / baseRadius), colours);
            }*/

            // draw hat outline
            colours.setStyle(Paint.Style.STROKE);
            colours.setStrokeWidth(5);
            colours.setARGB(255, 0, 0, 0);
            myCanvas.drawCircle(newX, newY, hatRadius, colours);

            colours.setStyle(Paint.Style.FILL);
            // loop to draw the hat for shade
            for (int i = 0; i <= (int) (hatRadius / ratio); i++) {
                // vary colour throughout loop for shading effect
                colours.setARGB(255, 255, (int) (i * (255 * ratio / hatRadius)), (int) (i * (255 * ratio / hatRadius)));
                myCanvas.drawCircle(newX, newY, hatRadius - (float) i * (ratio / 2), colours);
            }
            getHolder().unlockCanvasAndPost(myCanvas); // write new drawing to SurfaceView
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centreX, centreY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /** overrides the onTouch method in the OnTouchListener interface
     * this is called whenever user touches screen
     * gives you the view that the user touched and the where and how (tap, swipe etc)
     * @param v - a View
     * @param e - a MotionEvent
     */
    public boolean onTouch(View v, MotionEvent e) {
        if (Math.abs(centreX - e.getX()) <= (baseRadius / 4) && Math.abs(centreY - e.getY()) <= (baseRadius / 4)) {
            joystickHeld = true;
        }

        if (e.getAction() == e.ACTION_UP) {// ACTION_UP = when the user lifts finger off the screen
            joystickHeld = false;
        }

        if (v.equals(this)) { // make sure this listener only accepts touches on this SurfaceView
            if (!joystickHeld) { //|| (Math.abs(centreX - e.getX()) > baseRadius && Math.abs(centreY - e.getY()) > baseRadius)) {
                drawJoystick(centreX, centreY); // if user lets go of screen return joystick to centre
                //joystickCallBack.onJoystickMoved(0, 0, getId());
            } else {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centreX, 2)) + Math.pow(e.getY() - centreY, 2));
                if (displacement < baseRadius)
                {
                    drawJoystick(centreX, e.getY());
                    //drawJoystick(e.getX(), e.getY()); // constrain to y axis movement
                    //joystickCallBack.onJoystickMoved((e.getX() - centreX) / baseRadius, (e.getY() - centreY) / baseRadius, getId());
                } else {
                    float ratio = baseRadius / displacement;
                    //float constrainedX = centreX + (e.getX() - centreX) * ratio;
                    float constrainedY = centreY + (e.getY() - centreY) * ratio;
                    drawJoystick(centreX, constrainedY);
                    //joystickCallBack.onJoystickMoved((constrainedX - centreX) / baseRadius, (constrainedY - centreY) / baseRadius, getId());
                }
            }
            /*if (joystickHeld && Math.abs(centreX - e.getX()) <= baseRadius && Math.abs(centreY - e.getY()) <= baseRadius)
            {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centreX, 2)) + Math.pow(e.getY() - centreY, 2));
                if (displacement < baseRadius) {
                    drawJoystick(e.getX(), e.getY());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centreX + (e.getX() - centreX) * ratio;
                    float constrainedY = centreY + (e.getY() - centreY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                }
            } else {

            }*/
        }
        return true;
    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
