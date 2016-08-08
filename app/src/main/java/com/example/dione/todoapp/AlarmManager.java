package com.example.dione.todoapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by dione on 08/08/2016.
 */
public class AlarmManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm_received", "oh yeah");
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.littlethings_logo_white);
        mBuilder.setContentTitle(intent.getStringExtra("TITLE"));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    public void setAlarm(Context context, int year, int month, int date, int hour, int minute, int pi, String title){
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, date);


        cal.set(Calendar.MINUTE, minute);
        if (hour>11){
            cal.set(Calendar.HOUR, hour-12);
            cal.set(Calendar.AM_PM, Calendar.PM);
        }else {
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.AM_PM, Calendar.AM);
        }

        Intent intent = new Intent(context, AlarmManager.class);
        intent.putExtra("TITLE", title);
        PendingIntent sender = PendingIntent.getBroadcast(context, pi, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.app.AlarmManager am = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(android.app.AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }
}
