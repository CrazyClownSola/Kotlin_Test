package com.sola.github.kotlin.tonight.ui

import android.os.Bundle
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.databinding.ActivityRoundTestBinding

/**
 * Created by Sola
 * 2017/6/16.
 */

class EmptyActivity : RxBindingBaseActivity() {

    lateinit var binding: ActivityRoundTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_test)
    }

    override fun initExtras(extras_: Bundle) {

    }

    override fun doAfterView() {

    }

    override fun initBinding(layoutResID: Int) {
        binding = buildBinding()
        binding.setListener {
            when (it.id) {
                R.id.id_btn_start -> {
                    binding.idRoundView.start()
                }
                R.id.id_btn_end -> {
                    binding.idRoundView.destroy()
                }
            }
        }
    }

}