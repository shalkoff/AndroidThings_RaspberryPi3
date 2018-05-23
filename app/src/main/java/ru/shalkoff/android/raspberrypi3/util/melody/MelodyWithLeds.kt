package ru.shalkoff.android.raspberrypi3.util.melody

import android.os.Handler
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import java.io.Closeable

/**
 * Класс содержит логику запуска мелодии и светодиодов
 */
class MelodyWithLeds(var piezo: Piezo = Piezo(),
                     var leds: List<Gpio> = listOf<Gpio>(RainbowHat.openLedBlue(),
                             RainbowHat.openLedRed(),
                             RainbowHat.openLedGreen())) : Closeable {

    val NOTES = listOf(
            71, 71, 71, 71, 71, 71, 71, 64, 67, 71,
            69, 69, 69, 69, 69, 69, 69, 62, 66, 69,
            71, 71, 71, 71, 71, 71, 71, 73, 74, 77,
            74, 71, 69, 66, 64, 64)
    val DURATIONS = listOf(
            300, 50, 50, 300, 50, 50, 300, 300, 300, 300,
            300, 50, 50, 300, 50, 50, 300, 300, 300, 300,
            300, 50, 50, 300, 50, 50, 300, 300, 300, 300,
            300, 300, 300, 300, 600, 600)

    private val playHandler = Handler()
    private var playbackRunnable: Runnable? = null

    private var noteIndex = 0
    private var timeIndex = 0
    private var ledIndex = 0

    fun playMelodyWithLeds() {
        stop()
        playbackRunnable = Runnable {
            piezo.play(NOTES[noteIndex].toDouble(), DURATIONS[timeIndex] * 0.8)
            leds[ledIndex].value = true
            leds[prevIndex(ledIndex, leds.size)].value = false
            if (noteIndex == NOTES.size - 1) {
                leds[ledIndex].value = false
                piezo.stop()
            } else {
                playHandler.postDelayed(playbackRunnable, DURATIONS[timeIndex].toLong())
                timeIndex++
                noteIndex++
                ledIndex = nextIndex(ledIndex, leds.size)
            }
        }
        playHandler.post(playbackRunnable)
    }

    fun stop() {
        resetTimes()
        playbackRunnable?.let {
            playHandler.removeCallbacks(it)
        }
        leds.forEach { it.value = false }
        piezo.stop()
    }

    private fun resetTimes() {
        noteIndex = 0
        timeIndex = 0
        ledIndex = 0
    }

    private fun prevIndex(currIndex: Int, size: Int): Int {
        val prevIndex = currIndex - 1
        return if (prevIndex >= 0) prevIndex else size - 1
    }

    private fun nextIndex(currIndex: Int, size: Int): Int {
        val nextIndex = currIndex + 1
        return if (nextIndex < size) nextIndex else 0
    }

    override fun close() {
        playbackRunnable?.let {
            playHandler.removeCallbacks(it)
        }
        leds.forEach { it.close() }
        piezo.close()
    }
}