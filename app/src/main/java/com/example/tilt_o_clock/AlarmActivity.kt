package com.example.tilt_o_clock

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tilt_o_clock.ui.theme.TiltoclockTheme

class AlarmActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Play alarm sound
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound).apply {
            isLooping = true
            start()
        }

        setContent {
            TiltoclockTheme {
                TiltToDisableAlarmScreen(onAlarmDismissed = {
                    stopAlarm()
                    finish()
                })
            }
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }
}
