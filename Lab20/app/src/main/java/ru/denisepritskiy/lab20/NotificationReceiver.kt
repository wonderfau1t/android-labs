package ru.denisepritskiy.lab20

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val resetIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(MainActivity.KEY_TEXT_REPLY, "FFFFFF")
        }
        context.startActivity(resetIntent)
        NotificationManagerCompat.from(context).cancel(MainActivity.NOTIFICATION_ID)
    }
}
