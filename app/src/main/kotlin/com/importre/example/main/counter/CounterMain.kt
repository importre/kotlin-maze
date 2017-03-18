package com.importre.example.main.counter

import com.importre.example.R
import com.importre.maze.Back
import com.importre.maze.Sinks
import com.importre.maze.Sources
import com.importre.maze.clicks
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

fun counterMain(sources: Sources<CounterModel>): Sinks<CounterModel> {

    val inc = sources.event.clicks(R.id.buttonInc).map { +1 }
    val dec = sources.event.clicks(R.id.buttonDec).map { -1 }
    val model = Observable.merge(inc, dec)
        .scan(0) { acc: Int, value: Int -> acc + value }
        .withLatestFrom(sources.model,
            BiFunction { value: Int, model: CounterModel ->
                model.copy(value = value)
            })
        .cacheWithInitialCapacity(1)

    val navigation = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    return Sinks(model, navigation)
}
