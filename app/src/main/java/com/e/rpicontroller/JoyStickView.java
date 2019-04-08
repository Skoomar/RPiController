package com.e.rpicontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JoyStickView extends SurfaceView implements SurfaceHolder.Callback{

    public JoyStickView(Context context) {
        super(context);
    }

    public JoyStickView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public JoyStickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
