package com.sola.github.kotlin.kotlin_test.di

import com.sola.github.kotlin.domain.cases.UserCenterCase
import com.sola.github.kotlin.domain.cases.UserCenterCaseImpl
import com.sola.github.kotlin.kotlin_test.MainActivity
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Sola
 * 2017/5/31.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(MainCenterModule::class))
interface MainCenterComponent {


    fun inject(activity: MainActivity)
}

@Module
class MainCenterModule {

    @Provides
    @ActivityScope
    fun provideUserCase(impl: UserCenterCaseImpl)
            : UserCenterCase = impl
}