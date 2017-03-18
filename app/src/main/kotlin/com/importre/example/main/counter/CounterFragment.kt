package com.importre.example.main.counter

import android.os.Bundle
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseFragment
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_counter.*

class CounterFragment : BaseFragment(), MazeListener<CounterModel> {

    override val layoutId: Int = R.layout.fragment_counter

    private val maze by lazy { Maze(CounterModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        maze.attach(this, arrayOf(
            buttonInc.clicks().map { ClickEvent(R.id.buttonInc) },
            buttonDec.clicks().map { ClickEvent(R.id.buttonDec) },
            toolbar.navigationClicks().map { ClickEvent(R.id.homeAsUp) }
        ))
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<CounterModel>) = counterMain(sources)

    override fun render(prev: CounterModel, curr: CounterModel) {
        textValue.text = "${curr.value}"
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    override fun finish() = maze.finish()
}
