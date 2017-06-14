package com.sola.github.kotlin.tonight.ui

import android.os.Bundle
import com.sola.github.kotlin.tonight.MainApplication
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.adapter.MainPagerAdapter
import com.sola.github.kotlin.tonight.databinding.ActivityMainBinding
import com.sola.github.kotlin.tonight.di.MainCenterComponent
import com.sola.github.kotlin.tonight.di.MainCenterModule
import com.sola.github.kotlin.tonight.di.base.HasComponent
import com.sola.github.kotlin.tonight.enums.EMain
import com.sola.github.kotlin.tonight.enums.EnumList
import com.sola.github.kotlin.tonight.navigator.Navigator
import com.sola.github.kotlin.tonight.presenter.MainPresenter
import com.sola.github.kotlin.tools.utils.SolaLogger
import javax.inject.Inject

class MainActivity : RxBindingBaseActivity(), SolaLogger, HasComponent<MainCenterComponent> {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigator: Navigator

    lateinit var adapter: MainPagerAdapter

    @Inject
    lateinit var mainPresenter: MainPresenter

    lateinit var mainComponent: MainCenterComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(R.layout.activity_main)
    }

    private fun initComponent() {
        mainComponent =
                MainApplication.appComponent.plus(MainCenterModule())
        mainComponent.inject(this)
    }

    override fun initExtras(extras_: Bundle) {
    }

    override fun getComponent(): MainCenterComponent = mainComponent

    override fun doAfterView() {
        val mains: Array<EMain> =
                arrayOf(
                        EMain(EnumList.TAB_RECOMMEND, -1, getString(R.string.string_label_main_recommend))
//                        EMain(EnumList.TAB_CIRCLE, -1, getString(R.string.string_label_main_circle)),
//                        EMain(EnumList.TAB_MANAGE, -1, getString(R.string.string_label_main_my))
                )
        adapter = MainPagerAdapter(supportFragmentManager, mains)
        binding.adapter = adapter
    }

    override fun initBinding(layoutResID: Int) {
        binding = buildBinding()
    }
}
