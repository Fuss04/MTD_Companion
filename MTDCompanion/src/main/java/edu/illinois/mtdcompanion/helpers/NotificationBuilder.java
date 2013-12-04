package edu.illinois.mtdcompanion.helpers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import edu.illinois.mtdcompanion.R.drawable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for broadcasting notifications and toasts
 * @author Tom Olson
 */
public class NotificationBuilder {

	/**
	 * SLF4J Logger object for logging in NotificationBuilder class
	 */
	private static final Logger logger = LoggerFactory.getLogger(NotificationBuilder.class);

	/**
	 * Current context of Android application
	 */
	private Context context;

	/**
	 * Manages broadcasting of notifications
	 */
	private NotificationManager noteManager;

	/**
	 * Default constructor
	 * @param context Assigned to {@link #context}
	 */
	public NotificationBuilder(Context context) {
		noteManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		this.context = context;
	}

	/**
	 * Broadcasts notification
	 * @param notificationID Unique integer ID of notification
	 * @param title Title of notification
	 * @param text Text displayed in notification
	 * @param onGoing True or False if notification should permanently remain in notification tray
	 */
	public void showNotification(int notificationID, String title, String text, boolean onGoing) {
		NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(context);
		noteBuilder.setContentTitle(title);
		noteBuilder.setContentText(text);
		noteBuilder.setSmallIcon(drawable.launcher_icon);
		noteBuilder.setOngoing(onGoing);

		noteManager.notify(notificationID, noteBuilder.build());

		logger.debug("Broadcasted notification with ID: {}", Integer.toString(notificationID));
	}

	/**
	 * Cancels persistent notification
	 * @param notificationID Unique integer ID of notification
	 */
	public void cancelNotification(int notificationID) {
		noteManager.cancel(notificationID);
	}

	/**
	 * Displays toast
	 * @param text Text to be displayed in toast
	 * @param duration Duration for toast to be displayed
	 */
	public void showToast(String text, int duration) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
