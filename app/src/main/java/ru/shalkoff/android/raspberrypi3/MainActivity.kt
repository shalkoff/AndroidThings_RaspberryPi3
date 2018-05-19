package ru.shalkoff.android.raspberrypi3

import android.app.Activity
import android.os.Bundle
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import ru.shalkoff.android.raspberrypi3.firebase.MyFirebaseMessagingService

/**
 * Активность для управления raspberry pi 3
 */
class MainActivity : Activity() {

    private lateinit var releyGpio: Gpio
    private lateinit var httpdserver: HttpdServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initServer()
        initRelayGpio()
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        releyGpio.close()
    }

    private fun initServer() {
        httpdserver = HttpdServer()
        httpdserver.start()
    }

    private fun initRelayGpio() {
        releyGpio = PeripheralManager.getInstance().openGpio(BoardDefaults.gpioForRelay)
        releyGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }

    private fun initListeners() {
        RxBus.listen(MyFirebaseMessagingService.MessageEvent::class.java).subscribe({
            releyGpio.value = it.relayState
        })
    }
}
