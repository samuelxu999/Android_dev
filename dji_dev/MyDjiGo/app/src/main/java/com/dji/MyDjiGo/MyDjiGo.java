package com.dji.MyDjiGo;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dji.MyDjiGo.utils.ToastUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;
import dji.sdk.sdkmanager.BluetoothProductConnector;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * MyDjiGo main application which is mainly used to register dji sdk service
 */
public class MyDjiGo extends Application {

    public static final String TAG = MyDjiGo.class.getName();

    public static final String FLAG_CONNECTION_CHANGE = "fpv_tutorial_connection_change";

    private static BaseProduct mProduct;
    private static BluetoothProductConnector mBluetoothConnector = null;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);
    public Handler mHandler;
    private static MyDjiGo mInstance;

    /**
     * Gets instance of the specific product connected after the
     * API KEY is successfully validated. Please make sure the
     * API_KEY has been added in the Manifest
     */
    public static synchronized BaseProduct getProductInstance() {
        if (null == mProduct) {
            mProduct = DJISDKManager.getInstance().getProduct();
        }
        return mProduct;
    }

    public static synchronized BluetoothProductConnector getBluetoothProductConnector() {
        if (null == mBluetoothConnector) {
            mBluetoothConnector = DJISDKManager.getInstance().getBluetoothProductConnector();
        }
        return mBluetoothConnector;
    }

    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof Aircraft;
    }

    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof HandHeld;
    }

    public static synchronized Aircraft getAircraftInstance() {
        if (!isAircraftConnected()) return null;
        return (Aircraft) getProductInstance();
    }

    public static synchronized HandHeld getHandHeldInstance() {
        if (!isHandHeldConnected()) return null;
        return (HandHeld) getProductInstance();
    }

    public static MyDjiGo getInstance() {
        return mInstance;
    }

    public static Bus getEventBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
        //This is used to start SDK services and initiate SDK.
        //DJISDKManager.getInstance().registerApp(this, mDJISDKManagerCallback);
        /*
         * handles SDK Registration using the API_KEY
         */

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            DJISDKManager.getInstance().registerApp(this, mDJISDKManagerCallback);
        }
        mInstance = this;
    }

    /**
     * When starting SDK services, an instance of interface DJISDKManager.DJISDKManagerCallback will be used to listen to
     * the SDK Registration result and the product changing.
     */
    private DJISDKManager.SDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.SDKManagerCallback() {

        //Listens to the SDK registration result
        @Override
        public void onRegister(DJIError error) {

            if (error == DJISDKError.REGISTRATION_SUCCESS) {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_LONG).show();
                        ToastUtils.setResultToToast(getApplicationContext(), "Register Success");
                    }
                });

                DJISDKManager.getInstance().startConnectionToProduct();

            } else {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Register sdk fails, check network is available", Toast.LENGTH_LONG).show();
                        ToastUtils.setResultToToast(getApplicationContext(), "Register sdk fails, check network is available");
                    }
                });

            }
            Log.e("TAG", error.toString());
        }

        //Listens to the connected product changing, including two parts, component changing or product connection changing.
        @Override
        public void onProductChange(BaseProduct oldProduct, BaseProduct newProduct) {

            mProduct = newProduct;
            if (mProduct != null) {
                mProduct.setBaseProductListener(mDJIBaseProductListener);
            }

            notifyStatusChange();
        }
    };

    private BaseProduct.BaseProductListener mDJIBaseProductListener = new BaseProduct.BaseProductListener() {

        @Override
        public void onComponentChange(BaseProduct.ComponentKey key, BaseComponent oldComponent, BaseComponent newComponent) {

            if (newComponent != null) {
                newComponent.setComponentListener(mDJIComponentListener);
            }
            Log.d(TAG,
                    String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                            key,
                            oldComponent,
                            newComponent));
            notifyStatusChange();
        }

        @Override
        public void onConnectivityChange(boolean isConnected) {

            Log.d(TAG, "onProductConnectivityChanged: " + isConnected);
            notifyStatusChange();
        }

    };

    private BaseComponent.ComponentListener mDJIComponentListener = new BaseComponent.ComponentListener() {

        @Override
        public void onConnectivityChange(boolean isConnected) {
            Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
            notifyStatusChange();
        }

    };

    private void notifyStatusChange() {
        mHandler.removeCallbacks(updateRunnable);
        mHandler.postDelayed(updateRunnable, 500);
        //bus.post(new ConnectivityChangeEvent());
    }

    /*public static class ConnectivityChangeEvent {
    }*/

    private Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
            sendBroadcast(intent);
        }
    };
}
