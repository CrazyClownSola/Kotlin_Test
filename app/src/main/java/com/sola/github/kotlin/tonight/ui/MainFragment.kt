package com.sola.github.kotlin.tonight.ui

import android.content.Context
import android.view.View
import com.google.gson.Gson
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.databinding.FragmentMainBinding
import com.sola.github.kotlin.tonight.di.MainCenterComponent
import com.sola.github.kotlin.tonight.di.base.HasComponent
import com.sola.github.kotlin.tonight.navigator.BundleFactory_Factory
import com.sola.github.kotlin.tonight.navigator.Navigator
import com.sola.github.kotlin.tonight.presenter.MainPresenter
import com.sola.github.kotlin.tonight.utils.Key_Constant
import com.sola.github.kotlin.tonight.view.base.BaseView
import com.sola.github.kotlin.tools.adapter.OnRecyclerItemClickListener
import com.sola.github.kotlin.tools.adapter.RecyclerBaseAdapter
import com.sola.github.kotlin.tools.delegate.IRecyclerDelegate
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/6/7.
 */
class MainFragment : RxBindingFragment() {

    lateinit var binding: FragmentMainBinding

    lateinit var adapter: RecyclerBaseAdapter<IRecyclerDelegate>

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var navigator: Navigator

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HasComponent<*>) // 注意类型安全
            (context.getComponent() as MainCenterComponent).inject(this)
    }

    override fun setContentView(): Int = R.layout.fragment_main

    override fun inject_binding(view: View?) {
        binding = buildBinding(view!!)
    }

    override fun doAfterView() {
        adapter = RecyclerBaseAdapter<IRecyclerDelegate>(context)
        adapter.listener = object : OnRecyclerItemClickListener<IRecyclerDelegate> {
            override fun onClick(v: View, dto: IRecyclerDelegate) {
                if (dto is BaseView<*>)
                    if (dto.data is ClubInfoDTO)
                        navigator.switchActivity(context,
                                ClubDetailActivity::class.java,
                                BundleFactory_Factory.create().get()
                                        .putString(Key_Constant.KEY_CLUB_DETAIL, Gson().toJson(dto.data))
                                        .build())
            }
        }
        binding.adapter = adapter
        requestData()
    }

    private fun requestData() {
        presenter.requestClubList(
                {
                    adapter.refreshList(it)
                },
                {}
        )
    }


}