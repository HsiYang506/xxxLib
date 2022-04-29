package com.xxx.library

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.stream.Collectors

@RequiresApi(Build.VERSION_CODES.N)
fun Map<*, *>.map2str4k(): String? {
    return this.keys.stream().map { key: Any? -> key.toString() + "=" + this[key] }.collect(
        Collectors.joining(
            Collectors.joining(", ", "{", "}") as CharSequence
        )
    )
}

@RequiresApi(Build.VERSION_CODES.N)
fun map2str(map: Map<*, *>): String? {
    return map.keys.stream().map { key: Any? -> key.toString() + "=" + map[key] }.collect(
        Collectors.joining(
            Collectors.joining(", ", "{", "}") as CharSequence
        )
    )
}

fun List<Any>.list2str4k(): String? {
    return this.toTypedArray().contentToString()
}

fun String.str2json(): JSONObject? {
    return if (this.startsWith("{") and this.endsWith("}")) {
        JSONObject(this)
    } else
        null
}

fun String.str2jsonArray(): JSONArray? {
    return if (this.startsWith("[") and this.endsWith("]")) {
        JSONArray(this)
    } else
        null
}

