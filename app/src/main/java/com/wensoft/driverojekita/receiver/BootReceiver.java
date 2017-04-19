package com.wensoft.driverojekita.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wensoft.driverojekita.service.MyFirebaseMessagingService;

/**
 * Created by farhan on 2/8/17.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
        //Intent i = new Intent(context, SplashActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(i);

        /***** For start Service  ****/
        Intent myIntent = new Intent(context, MyFirebaseMessagingService.class);
        context.startService(myIntent);
    }
}