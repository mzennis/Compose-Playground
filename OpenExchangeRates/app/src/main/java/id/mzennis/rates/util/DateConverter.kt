package id.mzennis.rates.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Created by meyta.taliti on 29/10/23.
 */
object DateConverter {

    fun millisToDate(millis: Long, formatPattern: String = "EEEE, d MMMM h:mm a"): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochMilli(millis)
            DateTimeFormatter
                .ofPattern(formatPattern)
                .format(
                    instant.atZone(ZoneId.systemDefault())
                )
        } else {
            val sdf = SimpleDateFormat(formatPattern, Locale.getDefault())
            val date = Date(millis)
            sdf.timeZone = TimeZone.getDefault()
            sdf.format(date)
        }
    }
}