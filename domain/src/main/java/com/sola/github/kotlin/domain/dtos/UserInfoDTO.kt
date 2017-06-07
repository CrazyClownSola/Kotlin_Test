package com.sola.github.kotlin.domain.dtos

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
