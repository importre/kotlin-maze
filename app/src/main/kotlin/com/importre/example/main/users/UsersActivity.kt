package com.importre.example.main.users

import com.importre.example.base.BaseFragmentActivity

class UsersActivity : BaseFragmentActivity<UsersFragment>() {

    override val fragment: UsersFragment by lazy { UsersFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
