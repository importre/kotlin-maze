package com.importre.example.base

import android.os.Bundle
import com.importre.example.R
import com.importre.example.utils.set

abstract class BaseFragmentActivity<out T : BaseFragment> : BaseActivity() {

    protected abstract val fragment: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return
        set(fragment)
    }
}
