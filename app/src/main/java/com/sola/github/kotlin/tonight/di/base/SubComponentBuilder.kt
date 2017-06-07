package com.sola.github.kotlin.tonight.di.base

/**
 * Created by Sola
 * 2017/5/31.
 */
interface SubComponentBuilder<in Module, out Component> {

    fun moduleBuild(module: Module): SubComponentBuilder<Module, Component>

    fun build(): Component

}

interface HasSubComponentBuilders {

    fun getSubComponentBuild(type: Int, index: Int): SubComponentBuilder<Any, Any>
}

interface HasComponent<out C> {
    fun getComponent(): C
}
