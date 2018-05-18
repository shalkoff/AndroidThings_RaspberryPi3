package ru.shalkoff.android.raspberrypi3

import fi.iki.elonen.NanoHTTPD

/**
 * Простой сервер
 */
class HttpdServer(private val listener: OnFireTriggerListener) : NanoHTTPD(PORT) {

    interface OnFireTriggerListener {
        fun onFireTriggered()
        fun onArmTriggered()
    }

    override fun serve(session: IHTTPSession): Response {
        val parameters = session.parameters
        if (parameters["on"] != null) {
            listener.onFireTriggered()
        } else if (parameters["off"] != null) {
            listener.onArmTriggered()
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