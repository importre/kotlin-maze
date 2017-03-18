package com.importre.example.main.anim

import android.os.Bundle
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseFragment
import com.importre.example.utils.changeBgColorSmoothly
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checkedChanges
import kotlinx.android.synthetic.main.fragment_anim.*

class AnimFragment : BaseFragment(), MazeListener<AnimModel> {

    override val layoutId: Int = R.layout.fragment_anim

    private val maze by lazy { Maze(AnimModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            button.clicks()
                .map { ClickEvent(R.id.button) },
            check.checkedChanges()
                .map { CheckedEvent(R.id.check, it) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<AnimModel>) = animMain(sources)

    override fun render(prev: AnimModel, curr: AnimModel) {
        if (curr.checked) {
            root.changeBgColorSmoothly(prev.bgColor, curr.bgColor, 1000L)
            toolbar.changeBgColorSmoothly(prev.toolbarColor, curr.toolbarColor, 1000L)
        } else {
            root.setBackgroundColor(curr.bgColor)
            toolbar.setBackgroundColor(curr.toolbarColor)
        }
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    override fun finish() = maze.finish()
}
