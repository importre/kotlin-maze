package com.importre.maze

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent

class CheckedEvent(
    override val id: Int,
    val checked: Boolean
) : Event

class ClickEvent(
    override val id: Int
) : Event

class ScrollEvent(
    override val id: Int,
    val event: RecyclerViewScrollEvent
) : Event

class ScrollStateChangeEvent(
    override val id: Int,
    val state: Int,
    val page: Int
) : Event

class SelectionEvent(
    override val id: Int,
    val index: Int
) : Event

class ItemSelectionEvent<out T>(
    override val id: Int,
    val index: Int,
    val item: T
) : Event

class StartEvent(
    override val id: Int
) : Event

class TextChangeEvent(
    override val id: Int,
    val text: CharSequence
) : Event

class RefreshEvent(
    override val id: Int
) : Event

class SeekEvent(
    override val id: Int,
    val value: Int
) : Event
