package com.importre.example.base

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val disposables by lazy { CompositeDisposable() }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
