package com.sola.github.kotlin.tonight.di

import com.sola.github.kotlin.domain.cases.ClubCenterCase
import com.sola.github.kotlin.domain.cases.ClubCenterCaseImpl
import com.sola.github.kotlin.domain.cases.UserCenterCase
import com.sola.github.kotlin.domain.cases.UserCenterCaseImpl
import com.sola.github.kotlin.tonight.ui.ClubDetailActivity
import com.sola.github.kotlin.tonight.ui.MainActivity
import com.sola.github.kotlin.tonight.ui.MainFragment
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

    fun inject(fragment: MainFragment)
}

@Module
class MainCenterModule {

    @Provides
    @ActivityScope
    fun provideUserCase(impl: UserCenterCaseImpl)
            : UserCenterCase = impl

    @Provides
    @ActivityScope
    fun provideClubCase(impl: ClubCenterCaseImpl): ClubCenterCase = impl
}

@ActivityScope
@Subcomponent(modules = arrayOf(ClubCenterModule::class))
interface ClubCenterComponent {

    fun inject(activity: ClubDetailActivity)
}

@Module
class ClubCenterModule {

    @Provides
    @ActivityScope
    fun provideClubCase(impl: ClubCenterCaseImpl): ClubCenterCase = impl
}