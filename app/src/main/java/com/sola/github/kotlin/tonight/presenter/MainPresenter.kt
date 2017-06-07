package com.sola.github.kotlin.tonight.presenter

import com.google.gson.Gson
import com.sola.github.kotlin.domain.cases.UserCenterCase
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/5.
 */
class MainPresenter @Inject constructor(
        val userCase: UserCenterCase
) {

    fun requestUserInfo(userId: String, onNext: (String) -> Unit) {
        userCase.requestUserInfo(userId, {
            val gson = Gson()
            val str = gson.toJson(it)
            onNext.invoke(str)
        }, {

        })
    }

}