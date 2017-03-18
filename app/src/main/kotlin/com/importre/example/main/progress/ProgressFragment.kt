package com.importre.example.main.progress

import android.os.Bundle
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseFragment
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_progress.*
import java.util.*

class ProgressFragment : BaseFragment(), MazeListener<ProgressModel> {

    override val layoutId: Int = R.layout.fragment_progress

    private val maze by lazy { Maze(ProgressModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            buttonRun.clicks()
                .map { ClickEvent(R.id.buttonRun) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<ProgressModel>) = progressMain(sources)

    override fun render(prev: ProgressModel, curr: ProgressModel) {
        println(curr.progress)
        textProgress.text = String.format(Locale.US, "%d%%", curr.progress)
        progress.progress = curr.progress.toInt()
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    override fun finish() = maze.finish()
}
