package com.importre.example.main.users

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.importre.example.R
import com.importre.example.api.Api
import com.importre.example.app.MazeApp
import com.importre.example.base.BaseFragment
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

class UsersFragment : BaseFragment(), MazeListener<UsersModel> {

    override val layoutId: Int = R.layout.fragment_users

    private val maze by lazy { Maze(UsersModel()) }

    @Inject lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MazeApp.comp.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            buttonUser.clicks()
                .map { ClickEvent(R.id.buttonUser) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<UsersModel>) = usersMain(sources, api)

    override fun render(prev: UsersModel, curr: UsersModel) {
        if (curr.loading) {
            progress.show()
            return
        }

        progress.hide()

        textName.text = curr.user.name
        textEmail.text = curr.user.email

        textName.setTextSize(TypedValue.COMPLEX_UNIT_SP, curr.nameSize)
        textEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, curr.nameSize)
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    override fun finish() = maze.finish()
}
