package com.example.media.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.net.ConnectivityManagerCompat;

import java.util.Formatter;

/**
 * @author ldg
 * @date 2018/8/24
 */
public class Utils {

    public static String timeFormat(long time) {
        String t = "";
        time = time / 1000;
        int h = (int) (time / 3600);
        int m = (int) ((time / 60) % 60);
        int s = (int) ((time % 60));
        Formatter formatter = new Formatter();
        if (h > 0) {
            t = formatter.format("%d:%02d:%02d", h, m, s).toString();
        } else {
            t = formatter.format("%02d:%02d", m, s).toString();
        }
        return t;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            Network network = manager.getActiveNetwork();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            } else
                return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isNetConnected(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            Network network = manager.getActiveNetwork();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
                return capabilities == null ? false : capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            } else
                return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        }
        return false;
    }
}
