package com.sola.github.kotlin.tonight.navigator

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.View
import com.sola.github.kotlin.tonight.R
import javax.inject.Inject

/**
 * Created by Sola
 * 2017/5/23.
 */
@Suppress("unused")
class Navigator @Inject constructor() {

    fun switchActivity(
            context: Context,
            clazz: Class<Any>,
            bundle: Bundle?) {
        switchActivity(context, clazz, bundle, -1, R.anim.fade_in_left, R.anim.activity_back)
    }

    fun switchActivityForResult(
            context: Context,
            clazz: Class<Any>,
            bundle: Bundle?,
            requestCode: Int) {
        switchActivity(context, clazz, bundle, requestCode, R.anim.fade_in_left, R.anim.activity_back)
    }

    fun switchActivityForResult(
            context: Context,
            clazz: Class<Any>,
            bundle: Bundle?,
            requestCode: Int,
            targetInAnim: Int,
            currentOutAnim: Int) {
        switchActivity(context, clazz, bundle, requestCode, targetInAnim, currentOutAnim)
    }

    private fun switchActivity(
            context: Context,
            clazz: Class<Any>,
            bundle: Bundle?,
            requestCode: Int,
            targetInAnim: Int,
            currentOutAnim: Int,
            vararg pairs: Pair<View, String>) {
        val intent: Intent = Intent(context, clazz)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context is Activity) {
            val options: ActivityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(context, *pairs)
            options.toBundle().putAll(bundle)
            when (requestCode) {
                -1 ->
                    context.startActivity(intent, options.toBundle())
                else ->
                    (context).startActivityForResult(intent, requestCode, options.toBundle())
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, bundle)
        } else
            context.startActivity(intent)
        if (context is Activity)
            context.overridePendingTransition(targetInAnim, currentOutAnim)
    }
}

//inline fun <reified P : Context, reified T : Any> Navigator.startActivity(
//        bundle: Bundle?,
//        requestCode: Int,
//        targetInAnim: Int,
//        currentOutAnim: Int,
//        vararg pairs: Pair<View, String>
//) {
//    val intent: Intent = Intent(P, T)
//}
