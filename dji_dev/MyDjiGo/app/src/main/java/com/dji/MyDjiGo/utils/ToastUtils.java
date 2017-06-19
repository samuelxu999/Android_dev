package com.dji.MyDjiGo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DJI on 2/28/17.
 */

public class ToastUtils {
    private static Handler mUIHandler = new Handler(Looper.getMainLooper());

    public static void setResultToToast(final Context context, final String string) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setResultToText(final Context context,final TextView tv, final String s) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (tv == null) {
                    Toast.makeText(context, "tv is null", Toast.LENGTH_SHORT).show();
                } else {
                    tv.setText(s);
                }
            }
        });
    }
}
