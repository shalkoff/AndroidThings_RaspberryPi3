package ru.shalkoff.android.raspberrypi3.firebase

import android.util.Log
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.shalkoff.android.raspberrypi3.util.RxBus

/**
 * Класс для принятия сообщений Firebase
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    data class RelayEvent(val relayState: Boolean)
    data class DisplayMessageEvent(val message: String)
    data class PlaySoundEvent(val play: Boolean)

    /**
     * Метод вызывается, когда приходит очередное сообщение
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //Получаем сообщение
        Log.d(TAG, "From: " + remoteMessage.from)

        // Проверка на содержание сообщением, полезных данных
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            remoteMessage.data["relay"]?.let {
                RxBus.publish(RelayEvent(it.toBoolean()))
            }
            remoteMessage.data["messageDisplay"]?.let {
                RxBus.publish(DisplayMessageEvent(it))
            }
            remoteMessage.data["playSound"]?.let {
                RxBus.publish(PlaySoundEvent(it.toBoolean()))
            }

            if (true) {
                // Для длительных задач, более 10 секунд использовать Job
                scheduleJob()
            } else {
                // Обработка сообщения в течении 10 секунд
                handleNow()
            }
        }

        // Проверка на содержание сообщением, уведомления
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: " + it.body)
        }
    }

    /**
     * Запуск джоба
     */
    private fun scheduleJob() {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder()
                .setService(MyJobService::class.java)
                .setTag("my-job-tag")
                .build()
        dispatcher.schedule(myJob)
    }

    /**
     * Обработка события
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }
}