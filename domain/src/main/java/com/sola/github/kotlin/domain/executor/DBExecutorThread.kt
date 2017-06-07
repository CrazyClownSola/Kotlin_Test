package com.sola.github.kotlin.domain.executor

import java.util.concurrent.Executor

import io.reactivex.Scheduler

/**
 * Created by Sola
 * 2017/2/20.
 * 数据库线程队列
 */
interface DBExecutorThread : Executor {

    val scheduler: Scheduler
}
