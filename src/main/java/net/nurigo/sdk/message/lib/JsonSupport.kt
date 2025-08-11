package net.nurigo.sdk.message.lib

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * 중앙 집중식 Json 및 직렬화 설정.
 * LocalDateTime 은 ISO_LOCAL_DATE_TIME 문자열로 직렬화/역직렬화합니다.
 */
object JsonSupport {
    private object LocalDateTimeIsoSerializer : KSerializer<LocalDateTime> {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: LocalDateTime) {
            encoder.encodeString(value.format(formatter))
        }

        override fun deserialize(decoder: Decoder): LocalDateTime {
            val text = decoder.decodeString()
            // Lenient parsing: accept ISO_LOCAL_DATE_TIME, ISO_OFFSET_DATE_TIME, ISO_ZONED_DATE_TIME, ISO_INSTANT
            return runCatching { LocalDateTime.parse(text, formatter) }
                .recoverCatching { OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime() }
                .recoverCatching { ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME).toLocalDateTime() }
                .recoverCatching { LocalDateTime.ofInstant(Instant.parse(text), ZoneOffset.UTC) }
                .getOrElse { throw it }
        }
    }

    val serializersModule: SerializersModule = SerializersModule {
        contextual(LocalDateTime::class, LocalDateTimeIsoSerializer)
    }

    val json: Json = Json {
        coerceInputValues = true
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
        serializersModule = JsonSupport.serializersModule
    }
}


