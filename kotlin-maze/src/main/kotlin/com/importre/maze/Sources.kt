package com.importre.maze

import io.reactivex.subjects.BehaviorSubject

class Sources<Model>(val model: BehaviorSubject<Model>,
                     val event: EventStream) {

    fun finish() {
        model.onComplete()
        event.onComplete()
    }

    fun event(event: Event) = this.event.onNext(event)
}
