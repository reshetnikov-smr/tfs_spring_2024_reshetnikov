package ru.elnorte.tfs_spring_2024_reshetnikov

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

fun provideEmojisList(): List<String> {
    return listOf(
        "\uD83D\uDE03",
        "\uD83D\uDE43",
        "\uD83D\uDE0D",
        "\uD83E\uDD2A",
        "\uD83D\uDE12",
        "\uD83D\uDE0E",
        "\uD83E\uDD2E",
        "\uD83D\uDCA9",
        "\uD83E\uDDE1",
        "\uD83E\uDD1E",
        "\uD83D\uDC4D",
        "\uD83D\uDC4E",
        "\uD83D\uDC31",
        "\uD83E\uDD21",
        "\uD83D\uDC40",
        "😌", "😇", "🎃",
        String(Character.toChars("2049".toInt(radix = 16))),
    )
}

fun getCurrentDateAndTime(): Long {
    val date = LocalDateTime.now()
    val zoneId = ZoneId.systemDefault()
    return date.atZone(zoneId).toEpochSecond()
}

fun Long.asDateString(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    val date = Date(this * 1000)
    return dateFormat.format(date)
}


fun EditText.afterTextChanged(action: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            action.invoke(s.toString())
        }
    })
}

fun Int.isSubscribed() = this % 2 == 0

fun unicodeEmojiToHexString(input: String): String {
    return String.format("%04x", Character.codePointAt(input, 0))
}

fun hexStringToUnicodeEmoji(input: String): String {
    return String(Character.toChars(input.toInt(radix = 16)))
}

/**
 * elnorte's log function.
 * Log is accessible in logcat, with "ellog" filter
 * Will be deleted once app is ready
 *
 * @param T type parameter
 * @param item the message that will be written to log
 */
fun <T> ellog(item: T) {
    Log.d("ellog", item.toString())
}
