package com.importre.example.main.login

import android.util.Patterns
import com.importre.example.R
import com.importre.example.utils.shareReplay
import com.importre.maze.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

fun loginMain(sources: Sources<LoginModel>): Sinks<LoginModel> {

    val emailChange = sources.event
        .textChanges(R.id.inputEmail)
        .map(CharSequence::toString)
        .shareReplay(1)

    val emailError = emailChange
        .map(::getEmailError)
        .withLatestFrom(sources.model,
            BiFunction { error: Int, model: LoginModel ->
                model.copy(emailError = error)
            })

    val email = emailChange
        .withLatestFrom(sources.model,
            BiFunction { email: String, model: LoginModel ->
                model.copy(email = email)
            })

    val passwordChange = sources.event
        .textChanges(R.id.inputPassword)
        .map(CharSequence::toString)
        .shareReplay(1)

    val passwordError = passwordChange
        .map(::getPasswordError)
        .withLatestFrom(sources.model,
            BiFunction { error: Int, model: LoginModel ->
                model.copy(passwordError = error)
            })

    val password = passwordChange
        .withLatestFrom(sources.model,
            BiFunction { password: String, model: LoginModel ->
                model.copy(password = password)
            })

    val login = sources.event
        .clicks(R.id.buttonLogin)
        .map { Show("toast") }

    val back = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    val model = Observable
        .mergeArray(emailError, email, passwordError, password)
        .cacheWithInitialCapacity(1)

    val navigation = Observable
        .mergeArray(back, login)

    return Sinks(model, navigation)
}

fun getEmailError(email: String): Int {
    return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        R.string.empty
    } else {
        R.string.error_invalid_email
    }
}

fun getPasswordError(password: String): Int {
    return if (password.length < 8) {
        R.string.error_invalid_password
    } else {
        R.string.empty
    }
}
