package ru.denisepritskiy.lab21

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.denisepritskiy.lab21.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TimerService.TimerListener {
    private lateinit var binding: ActivityMainBinding
    private var timerService: TimerService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            timerService?.setListener(this@MainActivity) // Устанавливаем listener
            isBound = true
            updateUI()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
            timerService?.setListener(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ... код запроса разрешений без изменений ...

        Intent(this, TimerService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        binding.startButton.setOnClickListener {
            if (isBound) {
                val minutes = binding.minutesEditText.text.toString().toIntOrNull() ?: 0
                val seconds = binding.secondsEditText.text.toString().toIntOrNull() ?: 0
                val totalSeconds = minutes * 60 + seconds

                if (totalSeconds > 0) {
                    timerService?.startTimer(totalSeconds)
                } else {
                    Toast.makeText(this, "Введите корректное время", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Реализация TimerListener
    override fun onTimerTick(timeLeft: String) {
        runOnUiThread {
            binding.statusTextView.text = timeLeft
        }
    }

    override fun onTimerFinish() {
        runOnUiThread {
            updateUI()
            Toast.makeText(this, "Таймер завершен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        if (timerService?.isRunning == true) {
            binding.minutesEditText.isEnabled = false
            binding.secondsEditText.isEnabled = false
            binding.startButton.text = getString(R.string.stop)
        } else {
            binding.minutesEditText.isEnabled = true
            binding.secondsEditText.isEnabled = true
            binding.startButton.text = getString(R.string.start)
            binding.statusTextView.text = getString(R.string.ready_to_start)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}


