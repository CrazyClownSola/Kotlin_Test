package com.sola.github.kotlin.domain.repository

import com.sola.github.kotlin.domain.dtos.ClubDetailInfoDTO
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Sola
 * 2017/6/1.
 */
interface UserCenterRepository {

    fun requestUserInfo(userId: String): Maybe<UserInfoDTO>

}

interface ClubCenterRepository {

    fun requestClubList(): Observable<Collection<ClubInfoDTO>>

    fun requestClubDetail(id: Int): Single<ClubDetailInfoDTO>
}