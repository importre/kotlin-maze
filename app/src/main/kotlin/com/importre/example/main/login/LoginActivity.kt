package com.importre.example.main.login

import com.importre.example.base.BaseFragmentActivity

class LoginActivity : BaseFragmentActivity<LoginFragment>() {

    override val fragment: LoginFragment by lazy { LoginFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
