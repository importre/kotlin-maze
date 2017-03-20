# <div align="center"><img src="https://cloud.githubusercontent.com/assets/1744446/24075375/55b817e2-0c5d-11e7-823c-83095e684469.png" width=256><br>kotlin-maze</div>

[![Download][bintray-badge]][bintray-version]
[![Build Status][travis-badge]][travis-url]

> :steam_locomotive: A simple way to implement applications using observable streams


## Usage

### Create immutable view model

> Recommend create class as comparable like `data` class

```kotlin
data class HelloModel(
    val name: String = ""
)
```

### Implement `MazeListener`

```kotlin
class HelloFragment : BaseFragment(), MazeListener<HelloModel> {

    override val layoutId: Int = R.layout.fragment_hello

    // Don't be in lifecycle of `Fragment`/`Activity`
    // In this case, `BaseFragment` is set to `retainInstance = true` basically
    // Set initial view model
    private val maze by lazy { Maze(HelloModel()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Attach maze with user events
        // Event.id is used to filter in `main` function
        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            inputName.textChanges()
                .map { TextChangeEvent(R.id.inputName, it) }
        ))
    }

    override fun onDestroyView() {
        // Detach
        maze.detach()
        super.onDestroyView()
    }

    // Implement main function
    override fun main(sources: Sources<HelloModel>) = helloMain(sources)

    // Render view model
    override fun render(prev: HelloModel, curr: HelloModel) {
        val hello = getString(R.string.hello_message)
        textHello.text = hello.format(curr.name)
    }

    // Navigate somethings
    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
        }
    }

    // Cleanup if Activity is finished
    override fun finish() = maze.finish()

    // Handle errors
    override fun error(t: Throwable) {
        t.printStackTrace()
    }
}
```

### Implement main function

Implement main logic using `Observable`s

```kotlin
fun helloMain(sources: Sources<HelloModel>): Sinks<HelloModel> {

    val model = sources.event
        .textChanges(R.id.inputName)
        .map(CharSequence::toString)
        .withLatestFrom(sources.model,
            BiFunction { name: String, model: HelloModel ->
                model.copy(name = name)
            })
        .cacheWithInitialCapacity(1)

    val navigation = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    // `model` must be `ObservableCache`
    return Sinks(model, navigation)
}
```

### Customize `Navigation`s, `Event`s

You can extend `Navigation`s and/or `Event`s if you want

Please refer to default
<a href="/kotlin-maze/src/main/kotlin/com/importre/maze/Navigations.kt">`Navigation`s</a>,
<a href="/kotlin-maze/src/main/kotlin/com/importre/maze/Events.kt">`Event`s</a>


### More examples

- <a href="/app/src/main/kotlin/com/importre/example/main/hello">Hello, World!</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/counter">Counter</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/progress">Progress</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/anim">Animation</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/login">Login</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/users">HTTP</a>
- <a href="/app/src/main/kotlin/com/importre/example/main/photos">HTTP + Infinite Scrolling</a>
- <a href="/app/src/test/kotlin/com/importre/example">Tests</a>


## Install

```groovy
repositories {
    jcenter()
}

compile "com.importre:kotlin-maze:$maze_version"
```

### for test

```groovy
testCompile "com.importre:kotlin-maze-test:$maze_version"
```


## Slides

- [Speaker Deck][speakerdeck] - [Droid Knights 2017][droidknights]


## License

Apache 2.0 © Jaewe Heo



[icon]: https://cloud.githubusercontent.com/assets/1744446/24075375/55b817e2-0c5d-11e7-823c-83095e684469.png
[bintray-badge]: https://api.bintray.com/packages/importre/maven/kotlin-maze-test/images/download.svg
[bintray-version]: https://bintray.com/importre/maven/kotlin-maze-test/_latestVersion
[travis-badge]: https://travis-ci.org/importre/kotlin-maze.svg?branch=master
[travis-url]: https://travis-ci.org/importre/kotlin-maze
[speakerdeck]: https://speakerdeck.com/importre/compose-everything-with-rx-and-kotlin
[droidknights]: https://droidknights.github.io/2017/

