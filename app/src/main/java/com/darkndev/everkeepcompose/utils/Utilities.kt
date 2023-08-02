package com.darkndev.everkeepcompose.utils

import androidx.compose.ui.graphics.Color
import com.darkndev.everkeepcompose.ui.theme.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getFormatTime(modifiedMillis: Long): String {
    val modified =
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(modifiedMillis),
            ZoneId.systemDefault()
        )
    val now = ZonedDateTime.now()

    return if (modified.year != now.year) {
        modified.year.toString()
    } else if (modified.dayOfYear != now.dayOfYear) {
        DateTimeFormatter
            .ofPattern("MMM d", Locale.US)
            .format(modified)
    } else {
        DateTimeFormatter
            .ofPattern("h:mm a", Locale.US)
            .format(modified)
    }
}

fun getColor(ref: Int?): Pair<Color, Color> = when (ref) {
    0 -> Pair(Teal200, Teal700)
    1 -> Pair(Red200, Red700)
    2 -> Pair(Brown200, Brown700)
    3 -> Pair(LightBlue200, LightBlue700)
    4 -> Pair(Green200, Green700)
    5 -> Pair(Pink200, Pink700)
    6 -> Pair(DeepOrange200, DeepOrange700)
    7 -> Pair(Purple200, Purple700)
    else -> Pair(Color(0xFFFFFBFE), Color(0xFF1C1B1F))
}