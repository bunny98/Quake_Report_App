package com.example.android.quakereport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class CheckConnectivity extends BroadcastReceiver {
    private Boolean isConnected;
    @Override
    public void onReceive(Context context, Intent intent) {
        isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
    }
    public Boolean getIsConnected(){
        return isConnected;
    }
}
