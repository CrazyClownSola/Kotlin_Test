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

    fun putString(key: String, value: String): Bundle {
        bundle.putString(key, value)
        return bundle
    }

    fun putBundle(key: String?, value: Bundle): Bundle {
        bundle.putBundle(key, value)
        return bundle
    }

    fun putBoolean(key: String, value: Boolean): Bundle {
        bundle.putBoolean(key, value)
        return bundle
    }

    fun putByte(key: String, vararg value: Byte): Bundle {
        bundle.putByteArray(key, value)
        return bundle
    }

    fun putFloat(key: String, vararg value: Float): Bundle {
        bundle.putFloatArray(key, value)
        return bundle
    }

    fun putInt(key: String, vararg value: Int): Bundle {
        bundle.putIntArray(key, value)
        return bundle
    }

    fun putChar(key: String, vararg value: Char): Bundle {
        bundle.putCharArray(key, value)
        return bundle
    }

    fun putSerializable(key: String, value: Serializable): Bundle {
        bundle.putSerializable(key, value)
        return bundle
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