package ru.denisepritskiy.lab20

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity() {
    private lateinit var layout: RelativeLayout
    private lateinit var textView: TextView

    companion object {
        const val CHANNEL_ID = "color_channel"
        const val NOTIFICATION_ID = 1
        const val KEY_TEXT_REPLY = "key_text_reply"
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) showNotification()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.mainLayout)
        textView = findViewById(R.id.textViewColor)

        createNotificationChannel()

        findViewById<Button>(R.id.buttonNotify).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showNotification()
            }
        }
        handleColorChange(intent)
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleColorChange(intent)
    }
    private fun handleColorChange(intent: Intent?) {
        intent?.getStringExtra(KEY_TEXT_REPLY)?.let { color ->
            try {
                layout.setBackgroundColor(Color.parseColor("#$color"))
                textView.text = "Выбран цвет: #$color"
            } catch (e: Exception) {
                textView.text = "Неверный формат цвета"
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Сброс цвета
        val resetIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = "RESET_COLOR"
        }
        val resetPendingIntent = PendingIntent.getBroadcast(this, 0, resetIntent, PendingIntent.FLAG_IMMUTABLE)

        // Поле ввода
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("Введите цвет в формате RRGGBB")
            .build()

        val colorIntent = Intent(this, ColorInputReceiver::class.java).apply {
            action = "SET_COLOR"
            setPackage(packageName)
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_MUTABLE
        else
            PendingIntent.FLAG_UPDATE_CURRENT

        val replyPendingIntent = PendingIntent.getBroadcast(this, 1, colorIntent, flags)

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_notification, "Задать цвет", replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Настройка фона")
            .setContentText("Выберите действие:")
            .setContentIntent(contentIntent)
            .addAction(R.drawable.ic_notification, "Сбросить цвет", resetPendingIntent)
            .addAction(replyAction)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Color Notification"
            val descriptionText = "Канал уведомлений изменения цвета"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }
}
