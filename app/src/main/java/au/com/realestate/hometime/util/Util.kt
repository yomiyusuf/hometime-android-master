package au.com.realestate.hometime.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

/**
 * Date Extension function to get local time in 12hour format
 */
fun Date.toLocalTime(): String {
    val localDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    return localDateFormat.format(this)
}