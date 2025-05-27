package funn.j2k.oreControl.api.util

import com.google.gson.JsonElement

interface Parser<T> {
    fun toJson(value: T): JsonElement?

    fun fromJson(jsonElement: JsonElement?): T
}
