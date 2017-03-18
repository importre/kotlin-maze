package com.importre.maze.test

import com.importre.maze.Maze
import com.importre.maze.Sinks
import com.importre.maze.Sources
import io.reactivex.observers.TestObserver

/**
 * Makes [TestObserver] for [Maze] main function
 *
 * @param sources input streams
 * @param sinks output streams
 */
fun <Model> makeTestObserver(sources: Sources<Model>,
                             sinks: Sinks<Model>): TestObserver<Model> {
    val testObserver = TestObserver<Model>()
    val model = sinks.model.share()
    model.subscribe(sources.model)
    model.subscribe(testObserver)
    return testObserver
}
