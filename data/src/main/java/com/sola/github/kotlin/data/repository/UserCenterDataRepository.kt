package com.sola.github.kotlin.data.repository

import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import com.sola.github.kotlin.domain.repository.UserCenterRepository
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/1.
 */
class UserCenterDataRepository @Inject constructor() : UserCenterRepository {

    override fun requestUserInfo(userId: String): Maybe<UserInfoDTO> = Maybe.just(UserInfoDTO())

}
