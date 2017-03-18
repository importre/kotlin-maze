package com.importre.example.main.login

import android.os.Bundle
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseFragment
import com.importre.example.utils.toast
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit

class LoginFragment : BaseFragment(), MazeListener<LoginModel> {

    override val layoutId: Int = R.layout.fragment_login

    private val maze by lazy { Maze(LoginModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            inputEmail.textChanges()
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { TextChangeEvent(R.id.inputEmail, it) },
            inputPassword.textChanges()
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { TextChangeEvent(R.id.inputPassword, it) },
            buttonLogin.clicks()
                .map { ClickEvent(R.id.buttonLogin) },
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<LoginModel>) = loginMain(sources)

    override fun render(prev: LoginModel, curr: LoginModel) {
        inputEmailLayout.error = getString(curr.emailError)
        inputPasswordLayout.error = getString(curr.passwordError)

        val valid = arrayOf(curr.emailError, curr.passwordError)
            .count { it == R.string.empty } == 2
        buttonLogin.isEnabled = valid
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
            is Show -> {
                if (navigation.name == "toast") {
                    toast(R.string.login)
                }
            }
        }
    }

    override fun finish() = maze.finish()
}
