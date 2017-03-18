package com.importre.example.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.importre.example.R
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import io.reactivex.Observable
import java.security.InvalidParameterException
import kotlin.reflect.KClass

private val gson: Gson by lazy {
    GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()
}

fun Any.toJson(): String = gson.toJson(this)

fun Fragment.start(kClass: KClass<out Activity>, bundle: Bundle? = null) {
    val intent = Intent(activity, kClass.java)
    bundle?.let { intent.putExtras(it) }
    startActivity(intent)
}

fun AppCompatActivity.set(fragment: Fragment,
                          @LayoutRes container: Int = R.id.container) {
    supportFragmentManager
        .beginTransaction()
        .replace(container, fragment)
        .commit()
}

fun <T> Observable<T>.shareReplay(bufferSize: Int): Observable<T> {
    return replay(bufferSize).refCount()
}

fun Context.isPortrait(): Boolean {
    val orientation = resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Fragment.isPortrait(): Boolean = context.isPortrait()

fun ImageView.setUrl(url: String) {
    Glide.with(context).load(url).into(this)
}

fun filterScroll(scroll: RecyclerViewScrollEvent): Boolean {
    val lm = scroll.view().layoutManager
    val last = when (lm) {
        is StaggeredGridLayoutManager -> {
            lm.findLastVisibleItemPositions(null).first()
        }
        is GridLayoutManager -> {
            lm.findLastVisibleItemPosition()
        }
        is LinearLayoutManager -> {
            lm.findLastVisibleItemPosition()
        }
        else -> {
            throw InvalidParameterException()
        }
    }
    return last + 10 >= scroll.view().adapter.itemCount
}

fun View.changeBgColorSmoothly(from: Int, to: Int, duration: Long = 500L) {
    ValueAnimator.ofObject(ArgbEvaluator(), from, to)
        .apply {
            addUpdateListener({ animation ->
                animation?.run { setBackgroundColor(animatedValue as Int) }
            })
            this@apply.duration = duration
            start()
        }
}

fun Fragment.toast(@StringRes id: Int)
    = Toast.makeText(activity, id, Toast.LENGTH_SHORT).show()

fun Fragment.toast(message: String)
    = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

fun Context.browse(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri = Uri.parse(url)
    intent.data = uri
    startActivity(intent)
}

fun Fragment.browse(url: String) {
    context.browse(url)
}
