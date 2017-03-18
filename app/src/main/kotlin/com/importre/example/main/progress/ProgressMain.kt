package com.importre.example.main.progress

import com.importre.example.R
import com.importre.maze.Back
import com.importre.maze.Sinks
import com.importre.maze.Sources
import com.importre.maze.clicks
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

fun progressMain(sources: Sources<ProgressModel>): Sinks<ProgressModel> {

    val model = sources.event
        .clicks(R.id.buttonRun)
        .switchMap { Observable.interval(50, TimeUnit.MILLISECONDS).take(101) }
        .withLatestFrom(sources.model,
            BiFunction { progress: Long, model: ProgressModel ->
                model.copy(progress = progress)
            })
        .cacheWithInitialCapacity(1)

    val navigation = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    return Sinks(model, navigation)
}
