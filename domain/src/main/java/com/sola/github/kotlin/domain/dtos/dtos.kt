@file:Suppress("unused")

package com.sola.github.kotlin.domain.dtos

import com.google.gson.annotations.SerializedName

/**
 * Created by Sola
 * 2017/2/20.
 */
data class UserInfoDTO(
        var id: Int, var userId: String?,
        var userName: String?, var pic: String?,
        var gender: String?, var age: String?,
        var mobile: String?, var isVerified: Boolean) {

    // 测试用
    constructor() : this(-1, null, null, null, null, null, null, false)

    val isAdmin: Boolean
        get() = userId != null

}

data class ClubInfoDTO(
        @SerializedName("clubName") var name: String?,
        var id: Int,
        var pic: String?,
        var position: Pair<Int, Int>?,
        var distance: Int,
        var type: Int
) {
    constructor() : this(null, -1, null, null, -1, -1)
}

data class ClubDetailInfoDTO(
        var id: Int,
        var name: String?,
        var pic: String?,
        var per_capita: Int, // 人均
        var description: String?, // 基本描述
        var locationDes: String?,
        var position: Pair<Int, Int>?,
        var open_time: String?,
        var members: Int)