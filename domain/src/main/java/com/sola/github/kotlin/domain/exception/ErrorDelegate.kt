package com.sola.github.kotlin.domain.exception

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * Created by slove
 * 2016/12/19.
 */
interface ErrorDelegate {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    fun onError(): (Throwable) -> Unit

    fun onError(func: Action): (Throwable) -> Unit

    fun onError(func: (ErrorDTO) -> Unit): (Throwable) -> Unit

}

