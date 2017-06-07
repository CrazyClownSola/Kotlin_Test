package com.sola.github.kotlin.data.repository.exception


import com.sola.github.kotlin.domain.exception.ErrorDTO
import com.sola.github.kotlin.domain.exception.ErrorDelegate
import io.reactivex.functions.Action
import javax.inject.Inject

/**
 * Created by slove
 * 2016/12/19.
 */
class ErrorDelegateImpl
@Inject
constructor() : ErrorDelegate {

    override fun onError(): (Throwable) -> Unit = {

    }

    override fun onError(func: Action): (Throwable) -> Unit = {

    }

    override fun onError(func: (ErrorDTO) -> Unit): (Throwable) -> Unit = {
    }

}
