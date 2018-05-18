package ru.shalkoff.android.raspberrypi3

import android.os.Build

object BoardDefaults {
    private const val DEVICE_RPI3 = "rpi3"
    private const val DEVICE_IMX6UL_PICO = "imx6ul_pico"
    private const val DEVICE_IMX7D_PICO = "imx7d_pico"

    /**
     * Получить пин, по которому будет осуществляться управление
     */
    val gpioForRelay = when (Build.DEVICE) {
        DEVICE_RPI3 -> "BCM17"
        DEVICE_IMX6UL_PICO -> "GPIO4_IO22"
        DEVICE_IMX7D_PICO -> "GPIO2_IO02"
        else -> throw IllegalStateException("Unknown Build.DEVICE ${Build.DEVICE}")
    }
}
