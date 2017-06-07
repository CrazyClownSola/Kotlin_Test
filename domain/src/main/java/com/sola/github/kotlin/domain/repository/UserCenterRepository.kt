package com.sola.github.kotlin.domain.repository

import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * Created by Sola
 * 2017/6/1.
 */
interface UserCenterRepository {

    fun requestUserInfo(userId: String): Maybe<UserInfoDTO>

}