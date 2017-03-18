package com.importre.maze

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.observable.ObservableCache
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Provides a simple way to implement applications using observable streams
 *
 * @param initialModel initial model
 * @param modelScheduler default is [AndroidSchedulers.mainThread]
 */
class Maze<Model> @JvmOverloads constructor(
    private val initialModel: Model,
    private val modelScheduler: Scheduler = AndroidSchedulers.mainThread()) {

    internal val disposables = CompositeDisposable()
    internal val sources: Sources<Model> = Sources(
        BehaviorSubject.createDefault(initialModel),
        PublishSubject.create()
    )
    internal var listener: MazeListener<Model>? = null
    internal var sinks: Sinks<Model>? = null

    /**
     * Attaches model, navigation streams
     *
     * @param listener [MazeListener]
     * @param inputs input event streams
     */
    @JvmOverloads
    @Synchronized
    fun attach(listener: MazeListener<Model>,
               inputs: Array<Observable<out Event>>? = null) {
        this.listener = listener
        run(init(inputs))
    }

    /**
     * Clear all disposables
     */
    @Synchronized
    fun detach() {
        disposables.clear()
        this.listener = null
    }

    /**
     * Releases all resources
     */
    @Synchronized
    fun finish() {
        disposables.clear()
        sources.finish()
        sinks = null
        listener = null
    }

    /**
     * Sends an event to [EventStream]
     *
     * @param event input event
     */
    fun event(event: Event) = sources.event(event)

    private fun init(inputs: Array<Observable<out Event>>?): Sinks<Model> {
        initEventStream(inputs)
        initModelStream()
        return sinks ?: listener!!.main(sources).apply { sinks = this }
    }

    private fun initEventStream(inputs: Array<Observable<out Event>>?) {
        val streams = if (inputs == null || inputs.isEmpty()) {
            arrayOf(Observable.empty())
        } else {
            inputs
        }

        Observable
            .mergeArray(*streams)
            .subscribe({ event ->
                sources.event(event)
            }, { error ->
                handleError(error)
            })
            .apply { disposables.add(this) }
    }

    private fun initModelStream() {
        val initialModel = if (sources.model.hasValue()) sources.model.value else initialModel
        sources.model
            .debounce(10, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(modelScheduler)
            .scan(initialModel to initialModel) { prev, curr ->
                Pair(prev.second, curr)
            }
            .subscribe({ model ->
                synchronized(this@Maze) {
                    listener?.render(model.first, model.second)
                }
            }, { error ->
                handleError(error)
            })
            .apply { disposables.add(this) }
    }

    private fun run(sinks: Sinks<Model>) {
        assertSinks(sinks)

        sinks.model
            .subscribe({ model ->
                sources.model.onNext(model)
            }, { error ->
                handleError(error)
            })
            .apply { disposables.add(this) }

        sinks.navigation
            .subscribe({ nav ->
                synchronized(this@Maze) {
                    listener?.navigate(nav)
                }
            }, { error ->
                handleError(error)
            })
            .apply { disposables.add(this) }
    }

    private fun assertSinks(sinks: Sinks<Model>) {
        if (sinks.model !is ObservableCache<Model>) {
            throw AssertionError()
        }
    }

    private fun handleError(throwable: Throwable) {
        synchronized(this@Maze) {
            listener?.error(throwable) ?: throwable.printStackTrace()
        }
    }
}
