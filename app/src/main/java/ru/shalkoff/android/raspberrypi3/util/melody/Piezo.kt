package ru.shalkoff.android.raspberrypi3.util.melody

import android.os.Handler
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import java.io.Closeable

class Piezo(private val speaker: Speaker = RainbowHat.openPiezo(),
            private val stopHandler: Handler = Handler()) : Closeable {

    private var stopRunnable: Runnable? = null

    init {
        stopRunnable = Runnable { stop() }
    }

    fun play(frequency: Double) {
        speaker.play(frequency)
    }

    fun play(frequency: Double, duration: Double) {
        speaker.play(frequency)
        stopHandler.postDelayed(stopRunnable, duration.toLong())
    }

    fun stop() {
        speaker.stop()
    }

    override fun close() {
        stopHandler.removeCallbacks(stopRunnable)
        speaker.stop()
        speaker.close()
    }
}