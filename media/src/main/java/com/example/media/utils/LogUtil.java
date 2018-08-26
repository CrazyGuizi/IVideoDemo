package com.example.media.utils;

import android.util.Log;

/**
 * @author ldg
 * @date 2018/8/22
 */
public class LogUtil {

    private static boolean isDebug = true;

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }
}
