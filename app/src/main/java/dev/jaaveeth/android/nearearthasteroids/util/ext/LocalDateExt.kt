package dev.filipebezerra.android.nearearthasteroids.util.ext

import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object LocalDateExt {
    fun dateNow(): LocalDate = LocalDate.now()
    fun dateNowAsIsoLocalDate(): String = dateNow().asIsoLocalDate()

    fun oneWeekFromNow(): LocalDate = dateNow().plusWeeks(1)
}

fun LocalDate.asIsoLocalDate(): String = format(DateTimeFormatter.ISO_LOCAL_DATE)

fun LocalDate.toEpochMilli(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()