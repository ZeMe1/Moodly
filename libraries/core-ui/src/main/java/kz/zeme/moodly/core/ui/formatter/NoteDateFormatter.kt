package kz.zeme.moodly.core.ui.formatter

import android.content.Context
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.res.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class NoteDateFormatter(
    private val context: Context,
    private val activityProvider: ActivityProvider,
) {
    private val activeContext: Context
        get() = activityProvider.currentActivity ?: context

    fun toEpochDay(timestampMs: Long): Long =
        toLocalDate(timestampMs).toEpochDay()

    fun formatDayHeader(timestampMs: Long): String {
        val date = toLocalDate(timestampMs)
        val today = LocalDate.now()

        return when (date) {
            today -> activeContext.getString(R.string.date_today)
            today.minusDays(1L) -> activeContext.getString(R.string.date_yesterday)
            else -> {
                val pattern = if (date.year == today.year) "d MMMM" else "d MMMM yyyy"
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).format(date)
            }
        }
    }

    fun formatTime(timestampMs: Long): String {
        val dateTime = toLocalDateTime(timestampMs)
        return DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()).format(dateTime)
    }

    fun formatTodayWithTime(timestampMs: Long): String =
        activeContext.getString(R.string.date_today_time, formatTime(timestampMs))

    fun formatFullDateTime(timestampMs: Long): String {
        val dateTime = toLocalDateTime(timestampMs)
        return DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.getDefault()).format(dateTime)
    }

    fun formatCreatedAt(timestampMs: Long): String =
        activeContext.getString(R.string.date_created_at, formatFullDateTime(timestampMs))

    fun formatCategoryDateTime(timestampMs: Long): String {
        val dateTime = toLocalDateTime(timestampMs)
        val today = LocalDate.now()
        val pattern = if (dateTime.year == today.year) "d MMMM, HH:mm" else "d MMMM yyyy, HH:mm"
        return DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).format(dateTime)
    }

    private fun toLocalDate(timestampMs: Long): LocalDate =
        Instant.ofEpochMilli(timestampMs).atZone(ZoneId.systemDefault()).toLocalDate()

    private fun toLocalDateTime(timestampMs: Long): LocalDateTime =
        Instant.ofEpochMilli(timestampMs).atZone(ZoneId.systemDefault()).toLocalDateTime()
}
