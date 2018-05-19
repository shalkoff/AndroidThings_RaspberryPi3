package ru.shalkoff.android.raspberrypi3.firebase

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Класс для работы с токеном Firebase
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    /**
     * Метод вызывается, когда обновляется токен
     */
    override fun onTokenRefresh() {
        // Получение обновленного токена
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        //Уведомить сервер, о том, что токен обновился
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(token: String?) {
        //TODO
    }

    companion object {
        private val TAG = "MyFirebaseIIDService"
    }
}