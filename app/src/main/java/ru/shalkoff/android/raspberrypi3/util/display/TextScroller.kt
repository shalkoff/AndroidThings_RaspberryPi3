package ru.shalkoff.android.raspberrypi3.util.display

import android.util.Log

/**
 * Логика скролла текста, если он больше 4-х символов
 */
class TextScroller(text: String) {

    private var text = ""
    private var step: Int = 0
    private val dispLength: Int
    private var disp = ""

    init {
        var text = text
        dispLength = 4
        step = -1
        disp = ""
        if (!text.endsWith(" ")) {
            text += " "
        }
        this.text = prepareText(text)
    }

    fun step() {
        step++
        if (step >= text.length) {
            step = 0
        }
        val begin = step
        var temp = text.substring(begin, text.length)

        while (getBufferLength(temp) < dispLength) {
            temp += text
        }
        //truncate
        var temp2 = ""
        val charArray = temp.toCharArray()
        for (i in charArray.indices) {
            val c = charArray[i]
            temp2 += c
            if (getBufferLength(temp2) >= dispLength) {
                if (i + 1 < charArray.size && charArray[i + 1] == '.') {
                    temp2 += "."
                }
                break
            }
        }
        disp = temp2

        if (disp.startsWith(".")) {
            step()
        }
    }

    fun show() {
        Log.d("TextScroller", disp)
    }

    fun getText(): String {
        return disp
    }

    companion object {

        /**
         * Prepare the String for use in the TextScroller.
         * if there are 2 '.'-s after eachother, place space in between.
         *
         * @param s
         * @return
         */
        fun prepareText(s: String): String {
            var numChars = 0
            var buffer = ""
            for (c in s.toCharArray()) {
                if (c != '.') {
                    numChars++
                } else {
                    if (buffer.endsWith(".")) {
                        buffer += " "
                        numChars++
                    }
                }
                buffer += c
            }
            return buffer
        }

        fun getBufferLength(s: String): Int {
            var numChars = 0
            for (c in s.toCharArray()) {
                if (c != '.') {
                    numChars++
                }
            }
            return numChars
        }
    }
}