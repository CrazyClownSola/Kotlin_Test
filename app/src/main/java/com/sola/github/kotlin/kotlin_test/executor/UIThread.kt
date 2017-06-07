package com.sola.github.kotlin.kotlin_test.executor


import com.sola.github.kotlin.domain.executor.UIExecutorThread
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by slove
 * 2016/12/19.
 */
class UIThread @Inject constructor() : UIExecutorThread {

    override val scheduler: io.reactivex.Scheduler
        get() = AndroidSchedulers.mainThread()

}
