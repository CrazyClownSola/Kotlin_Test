package com.sola.github.kotlin.kotlin_test.executor

import com.sola.github.kotlin.domain.executor.NetExecutorThread
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import javax.inject.Inject


/**
 * author: Sola
 * 2015/10/28
 */
class NetExecutor
@Inject
constructor() : NetExecutorThread {

    // 线程池实例
    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        val workQueue = LinkedBlockingDeque<Runnable>()
        val threadFactory = JobThreadFactory()
        threadPoolExecutor = ThreadPoolExecutor(
                INITIAL_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME.toLong(), KEEP_ALIVE_TIME_UNIT,
                workQueue,
                threadFactory
        )
    }

    override fun execute(command: Runnable) {
        threadPoolExecutor.execute(command)
    }

    override val scheduler: Scheduler
        get() = Schedulers.from(this)

    private inner class JobThreadFactory : ThreadFactory {

        override fun newThread(r: Runnable): Thread {
            return Thread(r, "Android_")
        }
    }

    companion object {

        // ===========================================================
        // Constants
        // ===========================================================

        // 池中保存的线程数，包括空闲线程
        private val INITIAL_POOL_SIZE = 10
        // 线程池允许的最大线程数
        private val MAX_POOL_SIZE = 20
        // 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
        private val KEEP_ALIVE_TIME = 15
        // 参数的时间单位
        private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }

}
