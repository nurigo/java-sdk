package net.nurigo.sdk.message.lib

import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.*
import java.time.Instant as JavaInstant

fun Instant.toJavaInstant(): JavaInstant =
    JavaInstant.ofEpochMilli(this.toEpochMilliseconds())

@JvmOverloads
fun JavaInstant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(this, zoneId)
}

@JvmOverloads
fun Instant.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return LocalDateTime.ofInstant(this.toJavaInstant(), zoneId)
}