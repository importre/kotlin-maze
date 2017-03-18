package com.importre.maze

import io.reactivex.Observable

fun Observable<Event>.textChanges(id: Int): Observable<CharSequence> {
    return ofType(TextChangeEvent::class.java)
        .filter { it.id == id }
        .map { it.text }
}

fun Observable<Event>.clicks(id: Int): Observable<ClickEvent> {
    return ofType(ClickEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.checkedChanges(id: Int): Observable<CheckedEvent> {
    return ofType(CheckedEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.selects(id: Int): Observable<SelectionEvent> {
    return ofType(SelectionEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.itemSelects(id: Int): Observable<ItemSelectionEvent<*>> {
    return ofType(ItemSelectionEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.scrollStateChanges(id: Int): Observable<ScrollStateChangeEvent> {
    return ofType(ScrollStateChangeEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.scrollEvents(id: Int): Observable<ScrollEvent> {
    return ofType(ScrollEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.starts(id: Int): Observable<StartEvent> {
    return ofType(StartEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.refreshes(id: Int): Observable<RefreshEvent> {
    return ofType(RefreshEvent::class.java)
        .filter { it.id == id }
}

fun Observable<Event>.seeks(id: Int): Observable<SeekEvent> {
    return ofType(SeekEvent::class.java)
        .filter { it.id == id }
}
