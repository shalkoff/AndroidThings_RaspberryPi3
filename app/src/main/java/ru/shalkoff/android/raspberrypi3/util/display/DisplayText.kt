package ru.shalkoff.android.raspberrypi3.util.display

import android.os.Handler
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import java.io.Closeable
import java.io.IOException

/**
 * Класс содержит логику вывода сообщения на дисплей
 */
class DisplayText(var display: AlphanumericDisplay = RainbowHat.openDisplay()) : Closeable {

    private val mHandler = Handler()

    private var mTextScroller: TextScroller? = null
    private var textScrollerDelay: Long = 250 //ms

    init {
        display.clear()
        display.setEnabled(true)
    }

    private var runTextScrollerStep: Runnable = object : Runnable {
        override fun run() {
            mTextScroller?.let {
                it.step()
                try {
                    display.display(it.getText())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                mHandler.postDelayed(this, textScrollerDelay)
            }
        }
    }

    fun show(text: String) {
        clear()
        if (text.length > 4) {
            mTextScroller = TextScroller(text)
            mHandler.post(runTextScrollerStep)
        } else {
            display.display(text)
        }
    }

    fun clear() {
        display.clear()
        mHandler.removeCallbacks(runTextScrollerStep)
        mTextScroller = null
    }

    override fun close() {
        mHandler.removeCallbacks(runTextScrollerStep)
        mTextScroller = null
        display.close()
    }
}