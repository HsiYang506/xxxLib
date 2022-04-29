package com.xxx.library.log


fun Any.log() = XxxLog.wth(this)

fun <T> T.log(block: T.() -> Unit): T {
    XxxLog.wth(this ?: "error")
    block()
    return this
}