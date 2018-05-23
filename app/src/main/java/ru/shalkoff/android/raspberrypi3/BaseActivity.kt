package ru.shalkoff.android.raspberrypi3

import android.app.Activity
import android.os.Bundle
import android.util.Log
import java.io.IOException

/**
 * Базовый класс
 */
abstract class BaseActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runSafeIO {
            onCreateSafeIO()
        }
    }

    private fun runSafeIO(ioOperation: () -> Unit) {
        try {
            ioOperation()
        } catch (error: IOException) {
            Log.e("TAG", "IO Error", error)
        }
    }
    abstract fun onCreateSafeIO()
}