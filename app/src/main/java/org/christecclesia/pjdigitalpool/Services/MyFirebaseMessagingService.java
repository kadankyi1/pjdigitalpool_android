package org.christecclesia.pjdigitalpool.Services;


import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by zatana on 2/20/2017.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService  {

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("NotChatFCM", "=======================================================");

        if (!remoteMessage.getData().isEmpty() || ShortcutBadger.isBadgeCounterSupported(getApplicationContext())) {
            Log.e("NotChatFCM", "1");
            ShortcutBadger.applyCount(getApplicationContext(), 1);
        }
    }

    @Override
    public void onDeletedMessages() {

    }

}
