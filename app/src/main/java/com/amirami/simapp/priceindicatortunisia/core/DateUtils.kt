package com.amirami.simapp.priceindicatortunisia.core

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration


object DateUtils {

    fun getCurrentDateTime(): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        return datetimeInSystemZone.date.toString() +" "+ datetimeInSystemZone.time.toString()//datetimeInSystemZone.time.hour+ ":"+datetimeInSystemZone.time.minute + ":"+datetimeInSystemZone.time.second
    }


    fun getCurrentDateInMillis(): Int {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        return datetimeInSystemZone.time.toMillisecondOfDay()
    }

    fun getCurrentTime(): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        return datetimeInSystemZone.time.toString()
    }

    fun getCurrentDate(): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        return datetimeInSystemZone.date.toString()
    }




    fun durationSincePast(dateInPast : String):Int {
val dateinPast = dateInPast.ifEmpty { "2023-05-15 01:00:00" }
        val now = Clock.System.now()
//TODO WAIT UNTIL THERE IS A METHODE TO CONVERTE STRING TO ISO DATE IN KOTLINX DATE TIME
            val datePast = Instant.parse(dateinPast.substring(startIndex = 0, endIndex = 10).toLocalDate().toString()+"T01:00:00.475Z")


            val durationSinceThen: Duration = now - datePast




        return durationSinceThen.inWholeDays.toInt()
    }

}

