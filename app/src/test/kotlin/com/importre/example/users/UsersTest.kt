package com.importre.example.users

import com.importre.example.R
import com.importre.example.api.Api
import com.importre.example.main.users.UsersModel
import com.importre.example.main.users.usersMain
import com.importre.example.model.User
import com.importre.maze.ClickEvent
import com.importre.maze.Sources
import com.importre.maze.test.makeTestObserver
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UsersTest {

    @Mock lateinit var api: Api

    private lateinit var sources: Sources<UsersModel>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sources = Sources(
            BehaviorSubject.createDefault(UsersModel()),
            PublishSubject.create()
        )
    }

    @After
    fun tearDown() {
        sources.finish()
    }

    @Test
    fun testUiStream() {
        val user = User(0, "name", "a@a.com")
        val users = Observable.just(listOf(user))
        given(api.getUsers()).willReturn(users)

        // 1. initialize maze streams
        val sinks = usersMain(sources, api)

        // 2. make test observer
        val testObserver = makeTestObserver(sources, sinks)

        // 3. click
        sources.event(ClickEvent(R.id.buttonUser))

        // 4. tests
        testObserver.assertNoErrors()
        testObserver.assertValues(
            UsersModel(loading = true),
            UsersModel(user = user, loading = false)
        )
        testObserver.onComplete()
    }
}
