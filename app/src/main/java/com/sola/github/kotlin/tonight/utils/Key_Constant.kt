@file:Suppress("unused")

package com.sola.github.kotlin.tonight.utils

import com.google.gson.Gson

/**
 * Created by Sola
 * 2017/6/14.
 */

object Key_Constant {

    @JvmField val KEY_TITLE = "KEY_TITLE"

    @JvmField val KEY_MENU_ID = "KEY_MENU_ID"

    @JvmField val KEY_CLUB_DETAIL = "KEY_CLUB_DETAIL"
}

inline fun <reified T : Any> Gson.fromJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}