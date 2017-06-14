package com.sola.github.kotlin.tonight.navigator

import android.os.Bundle
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Sola
 * 2017/5/27.
 */
@Singleton
@Suppress("unused")
class BundleFactory @Inject constructor() {

    val bundle: Bundle = Bundle()

    fun build(): Bundle = bundle

    fun clear(): Bundle {
        bundle.clear()
        return bundle
    }

    fun putString(key: String, value: String): BundleFactory {
        bundle.putString(key, value)
        return this
    }

    fun putBundle(key: String?, value: Bundle): BundleFactory {
        bundle.putBundle(key, value)
        return this
    }

    fun putBoolean(key: String, value: Boolean): BundleFactory {
        bundle.putBoolean(key, value)
        return this
    }

    fun putByte(key: String, vararg value: Byte): BundleFactory {
        bundle.putByteArray(key, value)
        return this
    }

    fun putFloat(key: String, vararg value: Float): BundleFactory {
        bundle.putFloatArray(key, value)
        return this
    }

    fun putInt(key: String, vararg value: Int): BundleFactory {
        bundle.putIntArray(key, value)
        return this
    }

    fun putChar(key: String, vararg value: Char): BundleFactory {
        bundle.putCharArray(key, value)
        return this
    }

    fun putSerializable(key: String, value: Serializable): BundleFactory {
        bundle.putSerializable(key, value)
        return this
    }

//    companion object Factory { // 这个是静态实现,不推荐这么做
//        val mBundle: Bundle = Bundle()
//        fun create(): Bundle = mBundle
//        fun putString(key: String, value: String): Bundle {
//            mBundle.putString(key, value)
//            return mBundle
//        }
//    }

}