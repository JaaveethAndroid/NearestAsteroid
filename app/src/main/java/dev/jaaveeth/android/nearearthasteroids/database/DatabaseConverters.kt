package dev.filipebezerra.android.nearearthasteroids.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime

class DatabaseConverters {
    @TypeConverter
    fun localDateToEpochSecond(localDate: LocalDate): Long =
        localDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond()

    @TypeConverter
    fun epochSecondToLocalDate(epochSecond: Long): LocalDate =
        ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneOffset.UTC).toLocalDate()
}