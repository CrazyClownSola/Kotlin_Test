package com.sola.github.kotlin.tonight.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.sola.github.kotlin.domain.cases.ClubCenterCase
import com.sola.github.kotlin.domain.cases.UserCenterCase
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import com.sola.github.kotlin.domain.exception.ErrorDTO
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.ui.ClubInfoViewDTO
import com.sola.github.kotlin.tonight.view.base.BaseHolder
import com.sola.github.kotlin.tonight.view.base.BaseView
import com.sola.github.kotlin.tools.delegate.IRecyclerDelegate
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/5.
 */
class MainPresenter @Inject constructor(
        val userCase: UserCenterCase,
        val clubCase: ClubCenterCase
) {

    fun requestUserInfo(userId: String, onNext: (String) -> Unit) {
        userCase.requestUserInfo(userId, {
            val gson = Gson()
            val str = gson.toJson(it)
            onNext.invoke(str)
        }, {

        })
    }

    fun requestClubList(
            onNext: (Collection<IRecyclerDelegate>) -> Unit,
            onError: (ErrorDTO) -> Unit
    ) {
        clubCase.requestClubList(
                {
                    onNext.invoke(it.map
                    { ClubInfoViewDTO(it) })
                }, onError)
    }


}