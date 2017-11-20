package com.example.android.ChuffMeAuto;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

/**
 * The entry point to the BasicNotification sample.
 */
public class MainActivity extends Activity {
    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */
    public static final int NOTIFICATION_ID = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);
        sendNotificationToAndroidAuto ();
    }

    /**
     * The smallest amount of code that will send a notification to Android Auto
     */
    public void sendNotificationToAndroidAuto () {

        int thisConversationId = 42;
        Intent msgHeardIntent = new Intent().addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction("com.example.android.ChuffMeAuto.KANE_ACTION_MESSAGE_HEARD")
                .putExtra("conversation_id", thisConversationId);

        PendingIntent msgHeardPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                thisConversationId,
                msgHeardIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent msgReplyIntent = new Intent().addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction("com.example.android.ChuffMeAuto.KANE_ACTION_MESSAGE_REPLY")
                .putExtra("conversation_id", thisConversationId);

        PendingIntent msgReplyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                thisConversationId,
                msgReplyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput remoteInput = new RemoteInput.Builder("voice_reply_key")
                .setLabel("Prompt text")
                .build();

        String conversation = "Chuff Me";
        NotificationCompat.CarExtender.UnreadConversation.Builder unreadConvBuilder =
                new NotificationCompat.CarExtender.UnreadConversation.Builder(conversation)
                        .setReadPendingIntent(msgHeardPendingIntent)
                        .setReplyAction(msgReplyPendingIntent, remoteInput);

        unreadConvBuilder.addMessage("Chuff chuff, the trains are on time!").
                setLatestTimestamp(System.currentTimeMillis());

        // Below is everything to do with the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DummyChannelId");
        builder.extend(new NotificationCompat.CarExtender().setUnreadConversation(unreadConvBuilder.build()));
        builder.setSmallIcon(R.drawable.ic_stat_notification);
        // Set the intent that will fire when the user taps the notification.
        //builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        builder.setContentTitle("Notification to appear in Auto");
        builder.setContentText("Trains are on time!");
        builder.setSubText("More subtext");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
