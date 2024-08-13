package net.wifizer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifier {

	private static final String CHANNEL_ID = "1";

	private Context context;

	public Notifier(Context context) {
		this.context = context;
		createNotificationChannel();
	}

	private void createNotificationChannel() {
		// For Android O and above, create a notification channel
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Wifizer";
			String description = "Channel for Wifizer notifications";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);

			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	public void showNotification(String title, String message,String extras) {
		
        Intent intent = new Intent(context, MainActivity.class); // Replace with your activity
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
		.setSmallIcon(R.drawable.logo) // Replace with your notification icon
		.setContentTitle(title)
		.setContentText(message +"     " + extras)
		.setPriority(NotificationCompat.PRIORITY_DEFAULT)
		.setContentIntent(pendingIntent)
		.setAutoCancel(true);
		
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		notificationManager.notify(1, builder.build()); // 1 is the notification ID
    }
}