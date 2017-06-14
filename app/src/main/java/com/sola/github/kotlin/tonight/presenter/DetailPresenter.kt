package com.sola.github.kotlin.tonight.presenter

import com.sola.github.kotlin.domain.cases.ClubCenterCase
import com.sola.github.kotlin.domain.dtos.ClubDetailInfoDTO
import com.sola.github.kotlin.domain.exception.ErrorDTO
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/14.
 */
class DetailPresenter @Inject constructor(
        val clubCase: ClubCenterCase
) {

    fun requestClubDetailInfo(id: Int, onNext: (ClubDetailInfoDTO) -> Unit, onError: (ErrorDTO) -> Unit) {
        clubCase.requestClubDetail(id, onNext, onError)
    }

}