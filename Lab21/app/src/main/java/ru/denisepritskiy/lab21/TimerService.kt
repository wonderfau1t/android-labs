package ru.denisepritskiy.lab21

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TimerService : Service() {
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0
    var isRunning = false
    private val binder = TimerBinder()
    private val notificationId = 1
    private val channelId = "timer_channel"

    // Добавляем интерфейс для callback
    interface TimerListener {
        fun onTimerTick(timeLeft: String)
        fun onTimerFinish()
    }

    private var listener: TimerListener? = null

    fun setListener(listener: TimerListener?) {
        this.listener = listener
        // При подключении сразу отправляем текущее состояние
        if (isRunning) {
            listener?.onTimerTick(getFormattedTime())
        }
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder = binder

    fun startTimer(totalSeconds: Int) {
        stopTimer() // Всегда останавливаем предыдущий таймер

        remainingTime = (totalSeconds * 1000).toLong()
        isRunning = true

        createNotificationChannel()
        startForegroundServiceWithNotification()

        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification(getFormattedTime())
                listener?.onTimerTick(getFormattedTime())
            }

            override fun onFinish() {
                remainingTime = 0
                isRunning = false
                listener?.onTimerFinish()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        if (isRunning) {
            isRunning = false
            listener?.onTimerFinish()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    // ... остальные методы (getFormattedTime, createNotificationChannel и т.д.) без изменений ...

    fun getFormattedTime(): String {
        val seconds = (remainingTime / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Таймер",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Отсчет времени таймера"
                setShowBadge(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundServiceWithNotification() {
        val notification = buildNotification(getFormattedTime())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(notificationId, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(notificationId, notification)
        }
    }

    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Таймер")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun updateNotification(text: String) {
        val notification = buildNotification(text)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(notificationId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}