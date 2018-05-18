package com.mvp.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by luotao
 * 2018/1/19
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class ToastUtils {
    private static Toast sToast;
    private static long oneTime = 0;
    private static String oldMessage;

    @SuppressLint("ShowToast")
    public static void showToast(Context mContext, String message) {
        if (sToast == null && mContext != null) {
            sToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            sToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            long twoTime = System.currentTimeMillis();
            if (message.equals(oldMessage)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    sToast.show();
                }
            } else {
                sToast.setText(message);
                sToast.show();
                oldMessage = message;
            }
        }
    }
}
