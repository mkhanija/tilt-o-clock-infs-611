package com.example.tilt_o_clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tilt_o_clock.ui.theme.TiltoclockTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiltoclockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlarmScheduler()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScheduler() {
    val context = LocalContext.current

    // Initialize calendar with current time
    val calendar = remember { Calendar.getInstance() }

    // State to manage the selected time
    var selectedTime by remember { mutableStateOf(calendar.time) }

    // State to manage the visibility of the time picker dialog
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Increased padding for better aesthetics
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        Text(
            text = "Tilt-o-Clock",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Display the selected time
        Text(
            text = "Selected Time: ${java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime)}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to open time picker dialog
        Button(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth(0.7f) // Make button 70% of the width
        ) {
            Text(text = "Select Time")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Button to set the alarm
        Button(
            onClick = {
                calendar.time = selectedTime
                scheduleAlarm(context, calendar)
                Toast.makeText(
                    context,
                    "Alarm set for ${java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedTime)}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(text = "Set Alarm")
        }

        // Time Picker Dialog
        if (showTimePicker) {
            val is24HourFormat = android.text.format.DateFormat.is24HourFormat(context)
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = is24HourFormat,
                onTimeChange = { hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    selectedTime = calendar.time
                },
                onConfirm = {
                    showTimePicker = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean,
    onTimeChange: (Int, Int) -> Unit,
    onConfirm: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = is24Hour
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select Time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeChange(timePickerState.hour, timePickerState.minute)
                    onConfirm()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

fun scheduleAlarm(context: Context, calendar: Calendar) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // If the time is before now, set it for the next day
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}
