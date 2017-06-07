package com.sola.github.kotlin.domain.executor

import java.util.concurrent.Executor

import io.reactivex.Scheduler

/**
 * Created by slove
 * 2016/12/19.
 */
interface NetExecutorThread : Executor {
    val scheduler: Scheduler

}
