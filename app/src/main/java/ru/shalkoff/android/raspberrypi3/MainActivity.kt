package ru.shalkoff.android.raspberrypi3

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import ru.shalkoff.android.raspberrypi3.firebase.MyFirebaseMessagingService
import ru.shalkoff.android.raspberrypi3.server.HttpdServer
import ru.shalkoff.android.raspberrypi3.util.RxBus
import ru.shalkoff.android.raspberrypi3.util.display.DisplayText
import ru.shalkoff.android.raspberrypi3.util.melody.MelodyWithLeds

/**
 * Активность для управления raspberry pi 3
 */
class MainActivity : BaseActivity() {

    private lateinit var releyGpio: Gpio
    private lateinit var httpdserver: HttpdServer
    private lateinit var displayText: DisplayText
    private lateinit var melodyWithLeds: MelodyWithLeds

    override fun onCreateSafeIO() {
        initServer()
        initRelayGpio()
        initRainbowHat()
        initListeners()
    }

    override fun onDestroy() {
        releyGpio.close()
        displayText.close()
        melodyWithLeds.close()
        super.onDestroy()
    }

    private fun initServer() {
        httpdserver = HttpdServer()
        httpdserver.start()
    }

    private fun initRelayGpio() {
        releyGpio = PeripheralManager.getInstance().openGpio(BoardDefaults.gpioForRelay)
        releyGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
    }

    private fun initRainbowHat() {
        displayText = DisplayText()
        melodyWithLeds = MelodyWithLeds()
    }

    private fun initListeners() {
        RxBus.listen(MyFirebaseMessagingService.RelayEvent::class.java)
                .subscribe(
                        {
                            releyGpio.value = it.relayState
                        })
        RxBus.listen(MyFirebaseMessagingService.DisplayMessageEvent::class.java)
                .subscribe(
                        {
                            if (it.message.isEmpty()) {
                                displayText.clear()
                            } else {
                                displayText.show(it.message)
                            }
                        })
        RxBus.listen(MyFirebaseMessagingService.PlaySoundEvent::class.java)
                .subscribe(
                        {
                            if (it.play) {
                                melodyWithLeds.playMelodyWithLeds()
                            } else {
                                melodyWithLeds.stop()
                            }
                        })
    }
}
