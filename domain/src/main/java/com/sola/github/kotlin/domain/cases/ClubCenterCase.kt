package com.sola.github.kotlin.domain.cases

import com.sola.github.kotlin.domain.dtos.ClubDetailInfoDTO
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import com.sola.github.kotlin.domain.exception.ErrorDTO
import com.sola.github.kotlin.domain.exception.ErrorDelegate
import com.sola.github.kotlin.domain.executor.NetExecutorThread
import com.sola.github.kotlin.domain.executor.UIExecutorThread
import com.sola.github.kotlin.domain.repository.ClubCenterRepository
import com.sola.github.kotlin.domain.repository.UserCenterRepository
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/13.
 */

abstract class ClubCenterCase constructor(
        threadExecutor: NetExecutorThread,
        uiThread: UIExecutorThread,
        errorDelegate: ErrorDelegate
) : ComplexConnectionCase(threadExecutor, uiThread, errorDelegate) {

    abstract fun requestClubList(onNext: (Collection<ClubInfoDTO>) -> Unit, onError: (ErrorDTO) -> Unit)

    abstract fun requestClubDetail(id: Int, onNext: (ClubDetailInfoDTO) -> Unit, onError: (ErrorDTO) -> Unit)
}


class ClubCenterCaseImpl @Inject constructor(
        threadExecutor: NetExecutorThread,
        uiThread: UIExecutorThread,
        errorDelegate: ErrorDelegate,
        val repository: ClubCenterRepository) :
        ClubCenterCase(threadExecutor, uiThread, errorDelegate) {

    override fun requestClubDetail(id: Int, onNext: (ClubDetailInfoDTO) -> Unit, onError: (ErrorDTO) -> Unit) {
        execute_single(repository.requestClubDetail(id), onNext, onError)
    }

    override fun requestClubList(onNext: (Collection<ClubInfoDTO>) -> Unit, onError: (ErrorDTO) -> Unit) {
        execute(repository.requestClubList(), onNext, onError)
    }

}