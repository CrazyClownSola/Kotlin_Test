package com.sola.github.kotlin.domain.cases

import com.sola.github.kotlin.domain.exception.ErrorDTO
import com.sola.github.kotlin.domain.exception.ErrorDelegate
import com.sola.github.kotlin.domain.executor.UIExecutorThread
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

/**
 * Created by Sola
 * 2017/6/1.
 * 由于RxJava2对于操作符进行 部分区分，从原有的Observable仅此一家，调整成了百花齐放，各自继承接口也不同，因此通类方法中冗余方法变多了
 * [inline]内联函数，当闭包函数作为参数或者返回参数的时候，使用内联函数
 * （内联会将函数填充到代码中而不是开辟一个新的内存，本质上是通过增加代码量，去减少内存消耗）
 * [crossinline]
 */
@Suppress("unused")
abstract class ComplexConnectionCase constructor(
        val threadExecutor: Executor,
        val uiThread: UIExecutorThread,
        val errorDelegate: ErrorDelegate
) {

    fun getErrorAction(onError: (ErrorDTO) -> Unit): (Throwable) -> Unit =
            errorDelegate.onError { onError.invoke(it) }

    fun <T> execute(observable: Observable<T>,
                    onNext: (T) -> Unit,
                    onError: (ErrorDTO) -> Unit) {
        execute_return(observable, onNext, onError)
    }

    fun <T> execute_return(observable: Observable<T>,
                           onNext: (T) -> Unit,
                           onError: (ErrorDTO) -> Unit): Disposable
            = execute_throwable(observable, onNext, { getErrorAction { onError.invoke(it) } })

    fun <T> execute_throwable(observable: Observable<T>,
                              onNext: (T) -> Unit,
                              onError: (Throwable) -> Unit): Disposable
            = observable
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(uiThread.scheduler)
            .subscribe(
                    { onNext.invoke(it) },
                    { onError.invoke(it) })

    /**
     * 当传入的参数不是Observable形式的时候，内部进行一次create封装操作，确保数据处理在流中进行
     */
    fun <T, R> execute(func: Function<T, R>, obj: T,
                       onNext: (R) -> Unit,
                       onError: (ErrorDTO) -> Unit) {
        execute_return(func, obj, onNext, onError)
    }

    fun <T, R> execute_return(func: Function<T, R>, obj: T,
                              onNext: (R) -> Unit,
                              onError: (ErrorDTO) -> Unit): Disposable =
            execute_throwable(func, obj, onNext, { getErrorAction { onError.invoke(it) } })

    fun <T, R> execute_throwable(func: Function<T, R>, obj: T,
                                 onNext: (R) -> Unit,
                                 onError: (Throwable) -> Unit): Disposable =
            Observable.create<R> {
                if (!it.isDisposed) {
                    it.onNext(func.apply(obj))
                    it.onComplete()
                }
            }.subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(uiThread.scheduler)
                    .subscribe(
                            { onNext.invoke(it) },
                            { onError.invoke(it) })

    /**************************  Flowable流相关代码  ******************************/

    fun <T> execute_flow(observable: Flowable<T>,
                         onNext: (T) -> Unit,
                         onError: (ErrorDTO) -> Unit) {
        execute_flow_return(observable, onNext, onError)
    }

    fun <T> execute_flow_return(observable: Flowable<T>,
                                onNext: (T) -> Unit,
                                onError: (ErrorDTO) -> Unit): Disposable
            = execute_flow_throwable(observable, onNext, { getErrorAction { onError.invoke(it) } })

    fun <T> execute_flow_throwable(observable: Flowable<T>,
                                   onNext: (T) -> Unit,
                                   onError: (Throwable) -> Unit): Disposable
            = observable
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(uiThread.scheduler)
            .subscribe(
                    { onNext.invoke(it) },
                    { onError.invoke(it) })

    fun <T, R> execute_flow(
            func: Function<T, R>, obj: T,
            mode: BackpressureStrategy,
            onNext: (R) -> Unit,
            onError: (ErrorDTO) -> Unit) {
        execute_flow_return(func, obj, mode, onNext, onError)
    }

    fun <T, R> execute_flow(
            func: Function<T, R>, obj: T,
            onNext: (R) -> Unit,
            onError: (ErrorDTO) -> Unit) {
        execute_flow_return(func, obj, BackpressureStrategy.DROP, onNext, onError)
    }

    fun <T, R> execute_flow_return(
            func: Function<T, R>, obj: T, mode: BackpressureStrategy,
            onNext: (R) -> Unit,
            onError: (ErrorDTO) -> Unit): Disposable =
            execute_flow_throwable(func, obj, mode, onNext, getErrorAction { onError.invoke(it) })

    fun <T, R> execute_flow_throwable(
            func: Function<T, R>, obj: T, mode: BackpressureStrategy,
            onNext: (R) -> Unit,
            onError: (Throwable) -> Unit): Disposable =
            Flowable.create<R>({
                it.onNext(func.apply(obj))
                it.onComplete()
            }, mode)
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(uiThread.scheduler)
                    .subscribe(
                            { onNext.invoke(it) },
                            { onError.invoke(it) })

    /*************************** MayBe相关  *****************************/

    fun <T> execute_maybe(observable: Maybe<T>,
                          onNext: (T) -> Unit,
                          onError: (ErrorDTO) -> Unit) {
        execute_maybe_return(observable, onNext, onError)
    }

    fun <T> execute_maybe_return(observable: Maybe<T>,
                                 onNext: (T) -> Unit,
                                 onError: (ErrorDTO) -> Unit): Disposable
            = execute_maybe_throwable(observable, onNext, { getErrorAction { onError.invoke(it) } })

    fun <T> execute_maybe_throwable(observable: Maybe<T>,
                                    onNext: (T) -> Unit,
                                    onError: (Throwable) -> Unit): Disposable
            = observable
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(uiThread.scheduler)
            .subscribe(
                    { onNext.invoke(it) },
                    { onError.invoke(it) })

    fun <T, R> execute_maybe(
            func: Function<T, R>, obj: T,
            onNext: (R) -> Unit,
            onError: (ErrorDTO) -> Unit) {
        execute_maybe_return(func, obj, onNext, onError)
    }

    fun <T, R> execute_maybe_return(
            func: Function<T, R>, obj: T,
            onNext: (R) -> Unit,
            onError: (ErrorDTO) -> Unit): Disposable =
            execute_maybe_throwable(func, obj, onNext, getErrorAction { onError.invoke(it) })

    fun <T, R> execute_maybe_throwable(
            func: Function<T, R>, obj: T,
            onNext: (R) -> Unit,
            onError: (Throwable) -> Unit): Disposable =
            Maybe.create<R> {
                it.onSuccess(func.apply(obj))
                it.onComplete()
            }.subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(uiThread.scheduler)
                    .subscribe(
                            { onNext.invoke(it) },
                            { onError.invoke(it) })

    /*************************** Single相关  *****************************/

    inline fun <T> execute_single(observable: Single<T>,
                                  crossinline onNext: (T) -> Unit,
                                  crossinline onError: (ErrorDTO) -> Unit) {
        execute_single_return(observable, onNext, onError)
    }

    inline fun <T> execute_single_return(observable: Single<T>,
                                         crossinline onNext: (T) -> Unit,
                                         crossinline onError: (ErrorDTO) -> Unit): Disposable
            = execute_single_throwable(observable, onNext, { getErrorAction { onError.invoke(it) } })

    inline fun <T> execute_single_throwable(
            observable: Single<T>,
            crossinline onNext: (T) -> Unit,
            crossinline onError: (Throwable) -> Unit): Disposable
            = observable
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(uiThread.scheduler)
            .subscribe(
                    { onNext.invoke(it) },
                    { onError.invoke(it) })


}


//inline fun <reified T : Any> ComplexConnectionCase.execute() {
//    Observable.create<T>({
//        it.onNext()
//    })
//}