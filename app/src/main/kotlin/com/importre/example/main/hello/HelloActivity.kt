package com.importre.example.main.hello

import com.importre.example.base.BaseFragmentActivity

class HelloActivity : BaseFragmentActivity<HelloFragment>() {

    override val fragment by lazy { HelloFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
