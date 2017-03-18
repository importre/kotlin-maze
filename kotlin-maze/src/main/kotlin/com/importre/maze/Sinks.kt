package com.importre.maze

import io.reactivex.Observable

class Sinks<Model>(
    val model: Observable<Model>,
    val navigation: Observable<out Navigation>
)
