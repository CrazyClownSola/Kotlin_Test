package com.sola.github.kotlin.kotlin_test.di

/**
 * Created by Sola
 * 2017/5/23.
 */
class DataBaseComponent {

}

class StudyCode {

    // :Unit 表示返回参数
    fun printSum(a: Int, b: String): Unit {
        println("a is $a, b is $b")
    }

    // 等同于上述方法
    fun printSums(a: Int, b: String) = print("a is $a, b is $b")

    fun printSums(a: Int) = when (a) {
        1 -> {
            print("a is $a")
            true // 定义返回值是Boolean类型
        }
        else -> false
    }

    fun parseInt(str: String): Int? = str.toInt()

    fun parseIntNotNull(str: String?): Int = str!!.length // str如果为空 会抛出NPE异常

    var a: Int? = parseInt("2") as? Int // 安全转型

}