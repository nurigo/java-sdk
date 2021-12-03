package net.nurigo.solapi.sdk.message.extension

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.toStringValueMap(): Map<String, String> {
    val collections = mutableMapOf<String, String>()

    for (props in T::class.memberProperties) {
        val propKey = props.name
        val propValue = props.get(this)

        if (propValue is Collection<*>) {
            propValue.forEachIndexed { index, value ->
                // TODO: Nested Collection일 경우 어떻게 처리할지 고민 필요
                if (value == null) {
                    return@forEachIndexed
                }
                collections["${propKey}[${index}]"] = value.toString()
            }
        } else if (propValue !is Collection<*> && propValue != null) {
            collections[propKey] = propValue.toString()
        }
    }

    return collections
}