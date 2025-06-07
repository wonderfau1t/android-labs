package ru.denisepritskiy.lab20

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import androidx.core.app.NotificationManagerCompat

class ColorInputReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val color = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence(MainActivity.KEY_TEXT_REPLY)?.toString() ?: return

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(MainActivity.KEY_TEXT_REPLY, color)
        }
        context.startActivity(launchIntent)
        NotificationManagerCompat.from(context).cancel(MainActivity.NOTIFICATION_ID)
    }
}
