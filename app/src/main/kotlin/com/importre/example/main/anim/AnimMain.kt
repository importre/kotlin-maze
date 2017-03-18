package com.importre.example.main.anim

import android.graphics.Color
import com.importre.example.R
import com.importre.example.utils.shareReplay
import com.importre.maze.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*

fun animMain(sources: Sources<AnimModel>): Sinks<AnimModel> {

    val click = sources.event
        .clicks(R.id.button)
        .shareReplay(1)

    val bgColor = click
        .withLatestFrom(sources.model,
            BiFunction { _: Event, model: AnimModel ->
                model.copy(bgColor = Color.rgb(
                    Random().nextInt(255),
                    Random().nextInt(255),
                    Random().nextInt(255)
                ))
            })

    val toolbarColor = click
        .withLatestFrom(sources.model,
            BiFunction { _: Event, model: AnimModel ->
                model.copy(toolbarColor = Color.rgb(
                    Random().nextInt(255),
                    Random().nextInt(255),
                    Random().nextInt(255)
                ))
            })

    val checkSmooth = sources.event
        .checkedChanges(R.id.check).map { it.checked }
        .withLatestFrom(sources.model,
            BiFunction { checked: Boolean, model: AnimModel ->
                model.copy(checked = checked)
            })

    val back = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    val model = Observable
        .merge(bgColor, toolbarColor, checkSmooth)
        .cacheWithInitialCapacity(1)

    return Sinks(model, back)
}

