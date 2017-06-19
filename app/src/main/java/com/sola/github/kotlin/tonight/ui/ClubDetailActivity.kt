package com.sola.github.kotlin.tonight.ui

import android.os.Bundle
import com.google.gson.Gson
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.tonight.MainApplication
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.adapter.MainPagerAdapter
import com.sola.github.kotlin.tonight.databinding.ActivityClubDetailBinding
import com.sola.github.kotlin.tonight.di.ClubCenterModule
import com.sola.github.kotlin.tonight.presenter.DetailPresenter
import com.sola.github.kotlin.tonight.utils.Key_Constant
import com.sola.github.kotlin.tonight.utils.fromJson
import com.sola.github.kotlin.tools.utils.SolaLogger
import com.sola.github.kotlin.tools.utils.debug
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/13.
 */
class ClubDetailActivity : RxBindingBaseActivity(), SolaLogger {

    lateinit var binding: ActivityClubDetailBinding

    lateinit var club_info: ClubInfoDTO

    @Inject
    lateinit var presenter: DetailPresenter

    lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(R.layout.activity_club_detail)
    }

    private fun initComponent() {
        MainApplication.appComponent.plus(ClubCenterModule()).inject(this)
    }

    override fun initBinding(layoutResID: Int) {
        binding = buildBinding()
    }

    override fun initExtras(extras_: Bundle) {
        if (extras_.containsKey(Key_Constant.KEY_CLUB_DETAIL)) {
            val str = extras_.getString(Key_Constant.KEY_CLUB_DETAIL)
            if (!str.isNullOrEmpty()) {
                club_info = Gson().fromJson(str)
            }
        }
    }

    override fun doAfterView() {
        requestDetailInfo()
    }

    private fun requestDetailInfo() {
        presenter.requestClubDetailInfo(club_info.id,
                { binding.data = it },
                {})
    }

}