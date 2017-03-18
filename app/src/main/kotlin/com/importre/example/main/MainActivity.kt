package com.importre.example.main

import com.importre.example.base.BaseFragmentActivity

class MainActivity : BaseFragmentActivity<MainFragment>() {

    override val fragment by lazy { MainFragment() }
}
