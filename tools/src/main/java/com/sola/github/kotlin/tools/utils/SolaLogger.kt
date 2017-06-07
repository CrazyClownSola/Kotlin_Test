@file:Suppress("unused")

package com.sola.github.kotlin.tools.utils

import android.util.Log

/**
 * Created by Sola
 * 2017/6/5.
 * 效仿AnkoLogger(虽然这么说很不要脸，明显就是抄的)
 * 通过继承该类
 */
interface SolaLogger {
    val tag: String
        get() = getTag(javaClass)
}

fun SolaLogger(clazz: Class<*>): SolaLogger = object : SolaLogger {
    override val tag = getTag(clazz)
}

fun SolaLogger(tag: String): SolaLogger = object : SolaLogger {
    init {
        assert(tag.length <= 23)
    }

    override val tag = tag
}

// 这里reified修饰符表示该方法可以透过<>调用，同时由于inline的存在，定义的T类型可以在方法中使用
// inline是个神奇的东西，可以仔细研究研究
inline fun <reified T : Any> SolaLogger(): SolaLogger = SolaLogger(T::class.java)

fun SolaLogger.verbose(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.VERBOSE,
            { tag, msg -> Log.v(tag, msg) },
            { tag, msg, thr -> Log.v(tag, msg, thr) })
}

fun SolaLogger.debug(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.DEBUG,
            { tag, msg -> Log.d(tag, msg) },
            { tag, msg, thr -> Log.d(tag, msg, thr) })
}

fun SolaLogger.info(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.INFO,
            { tag, msg -> Log.i(tag, msg) },
            { tag, msg, thr -> Log.i(tag, msg, thr) })
}

fun SolaLogger.warn(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.WARN,
            { tag, msg -> Log.w(tag, msg) },
            { tag, msg, thr -> Log.w(tag, msg, thr) })
}

fun SolaLogger.error(message: Any?, thr: Throwable? = null) {
    log(this, message, thr, Log.ERROR,
            { tag, msg -> Log.e(tag, msg) },
            { tag, msg, thr -> Log.e(tag, msg, thr) })
}

inline fun SolaLogger.verbose(message: () -> Any?) {
    val tag = tag
    if (Log.isLoggable(tag, Log.VERBOSE))
        Log.v(tag, message()?.toString() ?: "null")
}

inline fun SolaLogger.debug(message: () -> Any?) {
    val tag = tag
    if (Log.isLoggable(tag, Log.DEBUG))
        Log.d(tag, message()?.toString() ?: "null")
}

inline fun SolaLogger.info(message: () -> Any?) {
    val tag = tag
    if (Log.isLoggable(tag, Log.INFO))
        Log.i(tag, message()?.toString() ?: "null")
}

inline fun SolaLogger.warn(message: () -> Any?) {
    val tag = tag
    if (Log.isLoggable(tag, Log.WARN))
        Log.w(tag, message()?.toString() ?: "null")
}

inline fun SolaLogger.error(message: () -> Any?) {
    val tag = tag
    if (Log.isLoggable(tag, Log.ERROR))
        Log.e(tag, message()?.toString() ?: "null")
}

private inline fun log(
        logger: SolaLogger,
        message: Any?,
        thr: Throwable?,
        level: Int,
        f: (String, String) -> Unit,
        fThrowable: (String, String, Throwable) -> Unit
) {
    val tag = logger.tag
    if (Log.isLoggable(tag, level))
        if (thr != null)
            fThrowable(tag, message?.toString() ?: "null", thr)
        else f(tag, message?.toString() ?: "null")
}

fun getTag(clazz: Class<*>): String {
    val tag = clazz.simpleName
    return if (tag.length <= 23) tag else tag.substring(0, 23)
}