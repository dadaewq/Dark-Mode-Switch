package com.modosa.switchnightui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.modosa.switchnightui.R;
import com.modosa.switchnightui.util.OpUtil;

public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForegroundService() {
        try {
            startForeground(R.string.title_permanentNotification, OpUtil.getPermanentNotification(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

