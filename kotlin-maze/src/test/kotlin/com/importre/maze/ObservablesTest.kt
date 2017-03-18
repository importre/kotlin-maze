package com.importre.maze

import io.reactivex.observers.TestObserver
import io.reactivex.subjects.ReplaySubject
import org.junit.Test

class ObservablesTest {

    @Test
    fun testTextChanges() {
        val eventStream = ReplaySubject.create<Event>()
        eventStream.onNext(ClickEvent(0))
        eventStream.onNext(TextChangeEvent(1, "hello"))
        eventStream.onNext(TextChangeEvent(1, "maze"))
        eventStream.onNext(TextChangeEvent(2, "world"))

        val testObserver = TestObserver<String>()
        eventStream.textChanges(1)
            .map(CharSequence::toString)
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValues("hello", "maze")

        eventStream.onComplete()
        testObserver.onComplete()
    }
}
