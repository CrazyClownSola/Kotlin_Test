package com.sola.github.kotlin.domain.cases

import com.sola.github.kotlin.domain.dtos.UserInfoDTO
import com.sola.github.kotlin.domain.exception.ErrorDTO
import com.sola.github.kotlin.domain.exception.ErrorDelegate
import com.sola.github.kotlin.domain.executor.NetExecutorThread
import com.sola.github.kotlin.domain.executor.UIExecutorThread
import com.sola.github.kotlin.domain.repository.UserCenterRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/1.
 */

abstract class UserCenterCase constructor(
        threadExecutor: NetExecutorThread,
        uiThread: UIExecutorThread,
        errorDelegate: ErrorDelegate
) : ComplexConnectionCase(threadExecutor, uiThread, errorDelegate) {

    fun test() {
//        Observable.just(null)
//                .subscribe(Consumer { t ->
//                    {
//
//                    }
//                }, Consumer { })
    }

    abstract fun requestUserInfo(
            userId: String,
            onNext: (UserInfoDTO) -> Unit,
            onError: (ErrorDTO) -> Unit)

}

class UserCenterCaseImpl @Inject constructor(
        threadExecutor: NetExecutorThread,
        uiThread: UIExecutorThread,
        errorDelegate: ErrorDelegate,
        val repository: UserCenterRepository) :
        UserCenterCase(threadExecutor, uiThread, errorDelegate) {

    override fun requestUserInfo(userId: String, onNext: (UserInfoDTO) -> Unit, onError: (ErrorDTO) -> Unit) {
        execute_maybe(repository.requestUserInfo(userId), onNext, onError)

    }

}