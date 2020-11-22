package org.christecclesia.pjdigitalpool.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.christecclesia.pjdigitalpool.Inc.Util;
import org.christecclesia.pjdigitalpool.Services.UpdateContent;

public class BootStartedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Util.show_log_in_console("FpBroadcastReceiver", "onReceive Started");
        Intent myIntent = new Intent(context, UpdateContent.class);
        context.startService(myIntent);

    }

}