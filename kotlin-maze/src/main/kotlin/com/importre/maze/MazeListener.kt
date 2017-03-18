package com.importre.maze

interface MazeListener<Model> {

    fun main(sources: Sources<Model>): Sinks<Model>

    fun render(prev: Model, curr: Model)

    fun navigate(navigation: Navigation)

    fun finish(): Unit

    fun error(t: Throwable) {
        t.printStackTrace()
    }
}
