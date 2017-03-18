package com.importre.example.main.progress

import com.importre.example.base.BaseFragmentActivity

class ProgressActivity : BaseFragmentActivity<ProgressFragment>() {

    override val fragment: ProgressFragment = ProgressFragment()

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
