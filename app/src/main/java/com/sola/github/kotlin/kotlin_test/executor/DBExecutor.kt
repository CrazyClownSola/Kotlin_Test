package com.sola.github.kotlin.kotlin_test.executor

import com.sola.github.kotlin.domain.executor.DBExecutorThread
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import javax.inject.Inject


/**
 * Created by 禄骥
 * 2016/5/20.
 */
class DBExecutor
@Inject
internal constructor() : DBExecutorThread {

    // ===========================================================
    // Fields
    // ===========================================================

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

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    override fun execute(command: Runnable) {
        threadPoolExecutor.execute(command)
    }

    override val scheduler: Scheduler
        get() = Schedulers.from(this)

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

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
        private val INITIAL_POOL_SIZE = 5
        // 线程池允许的最大线程数
        private val MAX_POOL_SIZE = 5
        // 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
        private val KEEP_ALIVE_TIME = 10
        // 参数的时间单位
        private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }
}
