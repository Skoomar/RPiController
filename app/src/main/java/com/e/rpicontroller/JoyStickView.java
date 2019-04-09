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

public class JoyStickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    private float centreX, centreY;
    private float baseRadius, hatRadius;
    private boolean joystickHeld;

    public JoyStickView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoyStickView(Context context, AttributeSet attributes)
    {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public JoyStickView(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    private void setupDimensions()
    {
        centreX = getWidth() / 2;
        centreY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight() / 4);
        hatRadius = Math.min(getWidth(), getHeight() / 7);
    }

    private void drawJoystick(float newX, float newY)
    {
        if (getHolder().getSurface().isValid()) // prevents drawing if SurfaceView hasn't been created on screen, preventing runtime errors
        {
            Canvas myCanvas = this.getHolder().lockCanvas(); // create canvas object
            Paint colours = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // clear background
            colours.setARGB(255, 50, 50, 50); // set brush to joystick base colours
            myCanvas.drawCircle(centreX, centreY, baseRadius, colours); // draw joystick base
            colours.setARGB(255, 0, 0, 255); // set brush to joystick top colours
            myCanvas.drawCircle(newX, newY, hatRadius,colours);
            getHolder().unlockCanvasAndPost(myCanvas); // write new drawing to SurfaceView
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setupDimensions();
        drawJoystick(centreX, centreY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    // overrides the onTouch method in the OnTouchListener interface
    // this is called whenever user touches screen
    // gives you the view that the user touched and the where and how (tap, swipe etc)
    public boolean onTouch(View v, MotionEvent e)
    {
        if (Math.abs(centreX - e.getX()) <= baseRadius && Math.abs(centreY - e.getY()) <= baseRadius)
        {
            joystickHeld = true;
        }

        if (e.getAction() == e.ACTION_UP) // ACTION_UP = when the user lifts finger off the screen
        {
            joystickHeld = false;
        }

        if (v.equals(this)) // make sure this listener only accepts touches on this SurfaceView
        {
            if (!joystickHeld || (Math.abs(centreX - e.getX()) > baseRadius && Math.abs(centreY - e.getY()) > baseRadius))
            {
                drawJoystick(centreX, centreY); // if user lets go of screen return joystick to centre
            } else {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centreX, 2)) + Math.pow(e.getY() - centreY, 2));
                if (displacement < baseRadius)
                {
                    drawJoystick(e.getX(), e.getY());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centreX + (e.getX() - centreX) * ratio;
                    float constrainedY = centreY + (e.getY() - centreY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
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
}
