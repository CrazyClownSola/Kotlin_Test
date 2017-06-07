package com.sola.github.kotlin.domain.executor

import io.reactivex.Scheduler

/**
 * Created by Sola
 * 2017/2/20.
 */
interface UIExecutorThread {

    val scheduler: Scheduler
}
