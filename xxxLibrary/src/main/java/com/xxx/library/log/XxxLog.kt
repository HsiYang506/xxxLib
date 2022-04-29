package com.xxx.library.log

import android.os.Build
import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 *
 *
 * @author x
 * @date 2022-4-8 13:31:18
 */

object XxxLog {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val NOTHING = 6
    private const val LEVEL = VERBOSE
    private const val SEPARATOR = ","

    fun wth(vararg vararg: Any) = when (vararg.size) {
        0 -> {
            d("empty")
        }
        1 -> {
            when (val parameter = vararg[0]) {
                is String -> {
                    json(parameter)
                }
                is Map<*, *> -> {
                    map(parameter)
                }
                is List<*> -> {
                    list(parameter)
                }
                else -> {
                    d(parameter)
                }
            }
        }
        2 -> {
            d(vararg[0].toString(), vararg[1])
        }
        else -> {
        }
    }


    private fun map(map: Map<*, *>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val joiner = StringJoiner(", ", "{ ", " }")
            map.toList().forEach { pair ->
                joiner.add("${pair.first} = ${pair.second}")
            }
            d(joiner.toString())
        } else {
            val builder = StringBuilder().apply {
                append("{")
                map.toList().forEachIndexed { index, pair ->
                    append("${pair.first} = ${pair.second}")
                    if (index != map.size - 1) {
                        append(",")
                    }
                }
                append("}")
            }
            d(builder.toString())
        }
    }

    private fun list(list: List<*>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val joiner = StringJoiner(", ", "[ ", " ]")
            list.forEach { item ->
                joiner.add("$item")
            }
            d(joiner.toString())
        } else {
            val builder = StringBuilder().apply {
                append("[ ")
                list.forEachIndexed { index, item ->
                    append("$item ")
                    if (index != list.size - 1) {
                        append(", ")
                    }
                }
                append(" ]")
            }
            d(builder.toString())
        }
    }

    private fun json(json: String) {
        if (json.startsWith("{") && json.endsWith("}")) {
            d("{Json Format} ->\r\n" + JSONObject(json).toString(2))
        } else if (json.startsWith("[") && json.endsWith("]")) {
            d("[JsonArray Format] ->\r\n" + JSONArray(json).toString(2))
        } else {
            d(json)
        }
    }


    private fun d(message: Any) {
        if (LEVEL <= DEBUG) {
            val stackTraceElement = getStackTrace()
            val tag = getDefaultTag(stackTraceElement)
            Log.d(tag, getLogInfo(stackTraceElement) + message)
        }
    }

    private fun d(_tag: String?, message: Any) {
        var tag = _tag
        if (LEVEL <= DEBUG) {
            val stackTraceElement = getStackTrace()
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement)
            }
            Log.d(tag, getLogInfo(stackTraceElement) + message)
        }
    }


    /**
     * 获取默认的TAG名称.
     * 比如在MainActivity.java中调用了日志输出.
     * 则TAG为MainActivity
     */
    private fun getDefaultTag(stackTraceElement: StackTraceElement): String {
        val fileName = stackTraceElement.fileName
        val stringArray = fileName.split("\\.").toTypedArray()
        return stringArray[0]
    }

    /**
     * 输出日志所包含的信息
     */
    private fun getLogInfo(stackTraceElement: StackTraceElement): String {
        val logInfoStringBuilder = StringBuilder()
        // 获取线程名
        val threadName = Thread.currentThread().name
        // 获取线程ID
        val threadID = Thread.currentThread().id
        // 获取文件名.即xxx.java
        val fileName = stackTraceElement.fileName
        // 获取类名.即包名+类名
        val className = stackTraceElement.className
        // 获取方法名称
        val methodName = stackTraceElement.methodName
        // 获取生日输出行数
        val lineNumber = stackTraceElement.lineNumber
        logInfoStringBuilder
            .append("-> ")
            .append(methodName)
            .append(" (")
            .append(fileName)
            .append(":")
            .append(lineNumber)
            .append(") ")
        return logInfoStringBuilder.toString()
    }

    /**
     * 排除当前类
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = 3
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (!name.contains("XxxLog")) {
                return --i
            }
            i++
        }
        return -1
    }

    private fun getStackTrace(): StackTraceElement {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = getStackOffset(sElements)
        stackOffset++
        return sElements[stackOffset]
    }
}