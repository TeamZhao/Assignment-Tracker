package com.example.assignmenttracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
 
public class NotificationPublisher extends BroadcastReceiver {
 
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static int mNotificationId = 0;
    public void onReceive(Context context, Intent intent) {
 
    	NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String notiTitle = intent.getStringExtra("Title");
		String notiContent = intent.getStringExtra("Content");
		// int id = intent.getIntExtra(NOTIFICATION_ID, 0);
		// notificationManager.notify(id, notification);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(notiTitle)
				.setContentText(notiContent);
		
		// Sets an ID for the notification
		mNotificationId ++;
		
		// Builds the notification and issues it.
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
 
    }
}