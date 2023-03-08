package com.bilmece.nekibuki.LoginNicknameSplashScreen;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {
    static boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        isNetworkAvailable(context);
    }

    @SuppressLint("LongLogTag")
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE); //Sistem ağını dinliyor internet var mı yok mu

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {

                        if (!isConnected) { //internet varsa
                            isConnected = true;
                        }
                        return true;
                    }
                }
            }
        }
        isConnected=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("İnternet Bağlantısı");
        builder.setMessage("Uygulamanın Düzgün Çalışmasını İstiyorsanız İnternet Bağlantınızı Kontrol Ediniz");
        builder.setNegativeButton("TAMAM", (dialog, id) -> System.exit(0));
        builder.show();
        return false;
    }
}