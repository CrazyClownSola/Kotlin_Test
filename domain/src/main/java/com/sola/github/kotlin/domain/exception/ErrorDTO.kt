package com.sola.github.kotlin.domain.exception

import java.io.Serializable

/**
 * Created by slove
 * 2016/12/19.
 * 通配的一个Error处理数据结构
 */
@Suppress("unused")
class ErrorDTO : Serializable {
    var errorMessage: String? = null

    var timestamp: String? = null

    var type: Int = 0

}
