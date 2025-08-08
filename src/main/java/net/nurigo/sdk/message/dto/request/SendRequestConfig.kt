package net.nurigo.sdk.message.dto.request

import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant

data class SendRequestConfig @JvmOverloads constructor(
    var scheduledDate: Instant? = null,
    var appId: String? = null,
    var allowDuplicates: Boolean = false,
    var showMessageList: Boolean = false,
) {

    @JvmOverloads
    constructor(
        scheduledDate: LocalDateTime? = null,
        appId: String? = null,
        allowDuplicates: Boolean = false,
        showMessageList: Boolean = false,
    ) : this(
        scheduledDate?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?.let { Instant.fromEpochMilliseconds(it) },
        appId,
        allowDuplicates,
        showMessageList
    )

    @JvmOverloads
    constructor(
        scheduledDate: java.time.Instant? = null,
        appId: String? = null,
        allowDuplicates: Boolean = false,
        showMessageList: Boolean = false,
    ) : this(
        scheduledDate?.toEpochMilli()?.let { Instant.fromEpochMilliseconds(it) },
        appId,
        allowDuplicates,
        showMessageList
    )

    fun setScheduledDate(scheduledDate: LocalDateTime) {
        this.scheduledDate = scheduledDate.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
            ?.let { Instant.fromEpochMilliseconds(it) }
    }

    fun setScheduledDate(scheduledDate: java.time.Instant) {
        this.scheduledDate = scheduledDate.toEpochMilli().let { Instant.fromEpochMilliseconds(it) }
    }
}