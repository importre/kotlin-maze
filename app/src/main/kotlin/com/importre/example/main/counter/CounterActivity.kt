package com.importre.example.main.counter

import com.importre.example.base.BaseFragmentActivity

class CounterActivity : BaseFragmentActivity<CounterFragment>() {

    override val fragment by lazy { CounterFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
