package com.sola.github.kotlin.tonight.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sola.github.kotlin.domain.dtos.ClubInfoDTO
import com.sola.github.kotlin.tonight.R
import com.sola.github.kotlin.tonight.view.base.BaseHolder
import com.sola.github.kotlin.tonight.view.base.BaseView

/**
 * Created by Sola
 * 2017/6/13.
 */
class ClubInfoViewDTO(item: ClubInfoDTO) : BaseView<ClubInfoDTO>(item) {

    override fun getHolder(context: Context, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = BaseHolder(LayoutInflater.from(context).inflate(R.layout.recycler_club_main_info, parent, false))
}