package com.importre.maze

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MazeTest {

    data class TestModel(val name: String = "")

    private lateinit var maze: Maze<TestModel>

    @Before
    fun setUp() {
        maze = Maze(TestModel(), TestScheduler())
    }

    @After
    fun tearDown() {
        maze.finish()
    }

    @Test
    fun testMazeLife() {
        val listener = object : MazeListener<TestModel> {
            override fun main(sources: Sources<TestModel>): Sinks<TestModel> {
                val model = Observable.empty<TestModel>()
                    .cacheWithInitialCapacity(1)
                val nav = Observable.empty<Navigation>()
                return Sinks(model, nav)
            }

            override fun render(prev: TestModel, curr: TestModel) = Unit
            override fun navigate(navigation: Navigation) = Unit
            override fun finish() = Unit
        }

        val modelObserver = TestObserver<TestModel>()
        val eventObserver = TestObserver<Event>()

        maze.sources.model.subscribe(modelObserver)
        maze.sources.event.subscribe(eventObserver)

        maze.attach(listener)
        val prevStream = maze.sinks
        assertNotNull(maze.sinks)

        maze.detach()
        assertNull(maze.listener)

        modelObserver.assertSubscribed()
        modelObserver.assertNotComplete()
        modelObserver.assertNotTerminated()

        maze.attach(listener)
        val currStream = maze.sinks
        assertNotNull(maze.sinks)

        assertEquals(currStream, prevStream)

        maze.finish()
        modelObserver.assertComplete()
        modelObserver.assertComplete()
        modelObserver.assertComplete()
    }

    @Test
    fun testMazeCallbacks() {
        val renderCounter = CountDownLatch(2)
        val navigationCounter = CountDownLatch(1)

        val listener = object : MazeListener<TestModel> {
            override fun main(sources: Sources<TestModel>): Sinks<TestModel> {
                val model = Observable.empty<TestModel>()
                    .cacheWithInitialCapacity(1)
                val nav = sources.event.starts(0)
                    .map { Back() }
                return Sinks(model, nav)
            }

            override fun render(prev: TestModel, curr: TestModel) {
                assertEquals(prev, curr)
                assertEquals(TestModel(), curr)
                renderCounter.countDown()
            }

            override fun navigate(navigation: Navigation) {
                navigationCounter.countDown()
                assertTrue(navigation is Back)
            }

            override fun finish() = Unit
        }

        maze.attach(listener)
        maze.detach()
        maze.attach(listener)
        maze.event(StartEvent(0))
        maze.detach()
        maze.finish()

        assertEquals(0, renderCounter.count)
        assertEquals(0, navigationCounter.count)
    }
}
