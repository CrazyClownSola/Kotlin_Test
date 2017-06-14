package com.sola.github.kotlin.tonight.di

import android.app.Application
import android.content.Context
import com.sola.github.kotlin.data.repository.ClubCenterDataRepository
import com.sola.github.kotlin.data.repository.UserCenterDataRepository
import com.sola.github.kotlin.data.repository.exception.ErrorDelegateImpl
import com.sola.github.kotlin.domain.exception.ErrorDelegate
import com.sola.github.kotlin.domain.executor.DBExecutorThread
import com.sola.github.kotlin.domain.executor.NetExecutorThread
import com.sola.github.kotlin.domain.executor.UIExecutorThread
import com.sola.github.kotlin.domain.repository.ClubCenterRepository
import com.sola.github.kotlin.domain.repository.UserCenterRepository
import com.sola.github.kotlin.tonight.executor.DBExecutor
import com.sola.github.kotlin.tonight.executor.NetExecutor
import com.sola.github.kotlin.tonight.executor.UIThread
import dagger.Component
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference
import javax.inject.Singleton

/**
 * Created by Sola
 * 2017/5/31.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        DomainModule::class,
        DataModule::class,
        RepositoryModule::class // 这里采用新的module的分组方式，是为了更加清晰化每个module模块组所负责的内容
))
@Suppress("unused")
interface AppComponent {

    fun inject(application: Application)

    // SubComponent的调用方式调整成这种模式
    fun plus(module: MainCenterModule): MainCenterComponent

    fun plus(module: ClubCenterModule): ClubCenterComponent

}

@Module
class AppModule(application: Application) {

    val mApplication: WeakReference<Context> = WeakReference(application)

    @Provides
    @Singleton
    fun provideApplication(): Context {
        return mApplication.get()!!
    }

    @Provides
    @Singleton
    fun provideNetExecutorThread(executor: NetExecutor): NetExecutorThread = executor

    @Provides
    @Singleton
    fun provideDBExecutorThread(executor: DBExecutor): DBExecutorThread = executor

    @Provides
    @Singleton
    fun provideUIExecutorThread(thread: UIThread): UIExecutorThread = thread

    @Provides
    @Singleton
    fun provideErrorDelegate(error: ErrorDelegateImpl): ErrorDelegate = error
}

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserCenterRepository(impl: UserCenterDataRepository)
            : UserCenterRepository = impl

    @Provides
    @Singleton
    fun provideClubCenterRepository(impl: ClubCenterDataRepository): ClubCenterRepository = impl

}

@Module
class DataModule

@Module
class DomainModule

/**
 * 在Kotlin当中想要使用SubComponent的时候需要注意
 * Sub的嵌套方式在java当中是用@MapKey和@IntoMap的方式去做的，然后这需要用到auto-value的库包
 * 这个库在Kotlin当中并不兼容，所以这里的实现方式调整一下
 */
//@Module(
//        subcomponents = arrayOf(
//                MainCenterComponent::class
//        )
//)
//@Suppress("unused")
//class SubComponentBindingModule {
//
//    @Provides
//    @Singleton
//    fun mainCenterComponent(builder: MainCenterComponent.Builder):
//            SubComponentBuilder<MainCenterComponent.MainCenterModule, MainCenterComponent> {
//        return builder.moduleBuild(MainCenterComponent.MainCenterModule)
//    }
//
//}
