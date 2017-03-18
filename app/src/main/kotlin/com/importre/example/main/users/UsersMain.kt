package com.importre.example.main.users

import com.importre.example.R
import com.importre.example.api.Api
import com.importre.example.model.Users
import com.importre.example.utils.shareReplay
import com.importre.maze.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*

fun usersMain(sources: Sources<UsersModel>, api: Api): Sinks<UsersModel> {

    val click = sources.event
        .clicks(R.id.buttonUser)
        .shareReplay(1)

    val loading = click
        .withLatestFrom(sources.model,
            BiFunction { _: ClickEvent, model: UsersModel ->
                model.copy(loading = true)
            })

    val users = click
        .switchMap { api.getUsers() }
        .withLatestFrom(sources.model,
            BiFunction { users: Users, model: UsersModel ->
                val index = Random().nextInt(users.size)
                model.copy(user = users[index], loading = false)
            })

    val back = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    val model = Observable
        .merge(loading, users)
        .cacheWithInitialCapacity(1)

    return Sinks(model, back)
}
