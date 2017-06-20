package com.dji.MyDjiGo.core;

import com.dji.MyDjiGo.MyDjiGo;

import dji.sdk.camera.Camera;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;

/**
 * FPVDemo is responsible for offer service to camera and video functionality.
 */
public class FPVDemo {

    public static final String TAG = FPVDemo.class.getName();

    /*
     * Get camera instance.
     */
    public static synchronized Camera getCameraInstance() {

        if (MyDjiGo.getProductInstance() == null) return null;

        Camera camera = null;

        if (MyDjiGo.getProductInstance() instanceof Aircraft) {
            camera = ((Aircraft) MyDjiGo.getProductInstance()).getCamera();

        } else if (MyDjiGo.getProductInstance() instanceof HandHeld) {
            camera = ((HandHeld) MyDjiGo.getProductInstance()).getCamera();
        }

        return camera;
    }
}
