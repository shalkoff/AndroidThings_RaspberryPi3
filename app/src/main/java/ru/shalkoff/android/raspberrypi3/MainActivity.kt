package ru.shalkoff.android.raspberrypi3

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager

/**
 * Активность для управления raspberry pi 3
 */
class MainActivity : Activity(), HttpdServer.OnFireTriggerListener {
    override fun onFireTriggered() {
        releyGpio.value = true
    }

    override fun onArmTriggered() {
        releyGpio.value = false
    }

    private lateinit var releyGpio: Gpio
    private lateinit var httpdserver: HttpdServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpdserver = HttpdServer(this)
        httpdserver.start()

        releyGpio = PeripheralManager.getInstance().openGpio(BoardDefaults.gpioForRelay)
        releyGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }

    override fun onDestroy() {
        super.onDestroy()
        releyGpio.close()
    }
}
