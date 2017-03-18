package com.importre.example.main.hello

import com.importre.example.R
import com.importre.maze.*
import io.reactivex.functions.BiFunction

fun helloMain(sources: Sources<HelloModel>): Sinks<HelloModel> {

    val model = sources.event
        .textChanges(R.id.inputName)
        .map(CharSequence::toString)
        .withLatestFrom(sources.model,
            BiFunction { name: String, model: HelloModel ->
                model.copy(name = name)
            })
        .cacheWithInitialCapacity(1)

    val navigation = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    return Sinks(model, navigation)
}
