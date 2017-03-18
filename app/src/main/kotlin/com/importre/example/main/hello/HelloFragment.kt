package com.importre.example.main.hello

import android.os.Bundle
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseFragment
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_hello.*

class HelloFragment : BaseFragment(), MazeListener<HelloModel> {

    override val layoutId: Int = R.layout.fragment_hello

    private val maze by lazy { Maze(HelloModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            inputName.textChanges()
                .map { TextChangeEvent(R.id.inputName, it) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<HelloModel>) = helloMain(sources)

    override fun render(prev: HelloModel, curr: HelloModel) {
        val hello = getString(R.string.hello_message)
        textHello.text = hello.format(curr.name)
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    override fun finish() = maze.finish()
}
