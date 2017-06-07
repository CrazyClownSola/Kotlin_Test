package com.sola.github.kotlin.kotlin_test.enums

import android.support.annotation.IntDef
import android.support.annotation.LayoutRes

/**
 * Created by Sola
 * 2017/5/31.
 */
//@MapKey(unwrapValue = false)
//annotation class SubMapKey(val intValue: Int, val indexValue: Int)

class EnumList {

    companion object {

        const val FLAG_TYPE_ACTIVITY = 0L

        const val FLAG_TYPE_CACHE = 1L

        const val FLAG_TYPE_DB = 2L

        const val TAB_RECOMMEND = 3L

        const val TAB_CIRCLE = 4L

        const val TAB_MANAGE = 5L

        const val TAB_MY_CIRCLE = 6L

        const val TAB_TOP_CIRCLE = 7L

    }

    @IntDef(FLAG_TYPE_ACTIVITY, FLAG_TYPE_CACHE, FLAG_TYPE_DB)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ESubType

    @IntDef(TAB_RECOMMEND, TAB_CIRCLE, TAB_MANAGE, TAB_MY_CIRCLE, TAB_TOP_CIRCLE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class EMainTabType
}

data class EMain(@EnumList.EMainTabType val type: Long, @LayoutRes val layout: Int, val title: String)