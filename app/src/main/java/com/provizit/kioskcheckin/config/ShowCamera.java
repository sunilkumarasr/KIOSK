package com.provizit.kioskcheckin.config;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    android.hardware.Camera camera ;
    SurfaceHolder holder;

    public ShowCamera(Context context, android.hardware.Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    //all mobiles
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera.Parameters parameters = camera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size msize = null;
        for (Camera.Size size : sizes){
            msize = size;
        }
        parameters.set("orientation","landscape");
        camera.setDisplayOrientation(90);
        parameters.setRotation(90);


        int w = 0, h = 0;
        for (Camera.Size size : sizes){
            if (size.width > w || size.height > h) {
                w = size.width;
                h = size.height;
            }
        }
        parameters.setPictureSize(w, h);

        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }


}
