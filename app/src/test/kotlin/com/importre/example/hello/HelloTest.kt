package com.importre.example.hello

import com.importre.example.R
import com.importre.example.main.hello.HelloModel
import com.importre.example.main.hello.helloMain
import com.importre.maze.*
import com.importre.maze.test.makeTestObserver
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class HelloTest {

    private lateinit var sources: Sources<HelloModel>

    @Before
    fun setUp() {
        sources = Sources(
            BehaviorSubject.createDefault(HelloModel()),
            PublishSubject.create()
        )
    }

    @After
    fun tearDown() {
        sources.finish()
    }

    @Test
    fun testUiStream() {
        val sinks = helloMain(sources)
        val testObserver = makeTestObserver(sources, sinks)

        // text input events
        charArrayOf('M', 'a', 'z', 'e').fold("") { text, char ->
            (text + char).apply {
                sources.event(TextChangeEvent(R.id.inputName, this))
            }
        }

        testObserver.assertNoErrors()
        testObserver.assertValues(
            HelloModel(name = "M"),
            HelloModel(name = "Ma"),
            HelloModel(name = "Maz"),
            HelloModel(name = "Maze")

        )
        testObserver.onComplete()
    }

    @Test
    fun testNavigationStream() {
        val sinks = helloMain(sources)
        val testObserver = TestObserver<Navigation>()
        sinks.navigation.subscribe(testObserver)

        // click event
        sources.event(ClickEvent(R.id.homeAsUp))

        testObserver.assertNoErrors()
        testObserver.assertValue { it is Back }
        testObserver.onComplete()
    }
}
