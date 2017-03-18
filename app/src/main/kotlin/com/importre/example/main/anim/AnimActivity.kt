package com.importre.example.main.anim

import com.importre.example.base.BaseFragmentActivity

class AnimActivity : BaseFragmentActivity<AnimFragment>() {

    override val fragment: AnimFragment by lazy { AnimFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
