package ru.shalkoff.android.raspberrypi3

import fi.iki.elonen.NanoHTTPD
import ru.shalkoff.android.raspberrypi3.firebase.MyFirebaseMessagingService

/**
 * Простой сервер
 */
class HttpdServer : NanoHTTPD(PORT) {

    override fun serve(session: IHTTPSession): Response {
        val parameters = session.parameters
        if (parameters["on"] != null) {
            RxBus.publish(MyFirebaseMessagingService.MessageEvent(1, true))
        } else if (parameters["off"] != null) {
            RxBus.publish(MyFirebaseMessagingService.MessageEvent(1, false))
        }

        val html = ("<html><head>"
                + "<script type=\"text/javascript\">"
                + " function on() { window.location = '?on=true'; }"
                + " function off() { window.location = '?off=true'; }"
                + "</script></head>"
                + "<body>"
                + "<button style=\"width: 50%; height: 100%; font-size: 4em;\" onclick=\"on();\">ON!</button>"
                + "<button style=\"width: 50%; height: 100%; font-size: 4em;\" onclick=\"off();\">OFF!</button>"
                + "</body></html>")

        return newFixedLengthResponse(html)
    }

    companion object {
        private val PORT = 8888
    }
}