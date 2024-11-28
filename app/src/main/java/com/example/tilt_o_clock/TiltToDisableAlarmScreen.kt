package com.example.tilt_o_clock

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun TiltToDisableAlarmScreen(onAlarmDismissed: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val gyroscopeSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    if (gyroscopeSensor == null) {
        // Display a message indicating the absence of a gyroscope
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Gyroscope sensor not available on this device.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    val cursorOffsetX = remember { mutableStateOf(0f) }
    val cursorOffsetY = remember { mutableStateOf(0f) }

    val cursorPosition = remember { mutableStateOf(Offset.Zero) }
    val targetPosition = remember { mutableStateOf(Offset.Zero) }

    val cursorSizeDp = 100.dp
    val targetSizeDp = 150.dp // Reduced size for easier overlap

    val density = LocalDensity.current
    val screenWidthPx = with(density) { context.resources.displayMetrics.widthPixels.toFloat() }
    val screenHeightPx = with(density) { context.resources.displayMetrics.heightPixels.toFloat() }

    val cursorSizePx = with(density) { cursorSizeDp.toPx() }
    val targetSizePx = with(density) { targetSizeDp.toPx() }

    val targetOffsetX = remember { mutableStateOf(0f) }
    val targetOffsetY = remember { mutableStateOf(0f) }

    // Randomize target position when composable is first composed
    LaunchedEffect(Unit) {
        targetOffsetX.value = Random.nextFloat() * (screenWidthPx - targetSizePx)
        targetOffsetY.value = Random.nextFloat() * (screenHeightPx - targetSizePx)
    }

    // Sensor event listener
    DisposableEffect(Unit) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                    cursorOffsetX.value += event.values[1] * 20f // Adjusted sensitivity
                    cursorOffsetY.value += event.values[0] * 20f

                    // Constrain cursor within screen bounds
                    cursorOffsetX.value = cursorOffsetX.value.coerceIn(0f, screenWidthPx - cursorSizePx)
                    cursorOffsetY.value = cursorOffsetY.value.coerceIn(0f, screenHeightPx - cursorSizePx)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_GAME
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    // Calculate the distance between the centers of the cursor and target
                    val cursorCenter = cursorPosition.value + Offset(cursorSizePx / 2, cursorSizePx / 2)
                    val targetCenter = targetPosition.value + Offset(targetSizePx / 2, targetSizePx / 2)

                    val distance = distanceBetweenPoints(cursorCenter, targetCenter)
                    val threshold = (cursorSizePx / 2 + targetSizePx / 2) * 0.8f // Allow some overlap margin

                    Log.d("TiltToDisableAlarm", "Distance: $distance, Threshold: $threshold")

                    if (distance < threshold) {
                        Log.d("TiltToDisableAlarm", "Overlap detected. Dismissing alarm.")
                        onAlarmDismissed()
                    } else {
                        Log.d("TiltToDisableAlarm", "No overlap detected.")
                    }
                }
            }
    ) {
        // Target area
        Box(
            modifier = Modifier
                .size(targetSizeDp)
                .offset { IntOffset(targetOffsetX.value.roundToInt(), targetOffsetY.value.roundToInt()) }
                .background(Color.Red, shape = CircleShape)
                .onGloballyPositioned { layoutCoordinates ->
                    targetPosition.value = layoutCoordinates.positionInRoot()
                }
        )

        // Cursor
        Box(
            modifier = Modifier
                .size(cursorSizeDp)
                .offset { IntOffset(cursorOffsetX.value.roundToInt(), cursorOffsetY.value.roundToInt()) }
                .background(Color.Blue, shape = CircleShape)
                .onGloballyPositioned { layoutCoordinates ->
                    cursorPosition.value = layoutCoordinates.positionInRoot()
                }
        )

        // Instruction text
        Text(
            text = "Tilt your device to move the blue circle over the red circle, then tap to dismiss the alarm.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
    }
}

fun distanceBetweenPoints(p1: Offset, p2: Offset): Float {
    val dx = p1.x - p2.x
    val dy = p1.y - p2.y
    return sqrt(dx * dx + dy * dy)
}
