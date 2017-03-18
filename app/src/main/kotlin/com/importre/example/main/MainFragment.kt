package com.importre.example.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.importre.example.R
import com.importre.example.base.BaseActivity
import com.importre.example.base.BaseFragment
import com.importre.example.main.anim.AnimActivity
import com.importre.example.main.counter.CounterActivity
import com.importre.example.main.hello.HelloActivity
import com.importre.example.main.login.LoginActivity
import com.importre.example.main.photos.PhotosActivity
import com.importre.example.main.progress.ProgressActivity
import com.importre.example.main.users.UsersActivity
import com.importre.example.utils.browse
import com.importre.example.utils.start
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = activity as? BaseActivity ?: return
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        val hello = buttonHello.clicks().map { HelloActivity::class }
        val counter = buttonCounter.clicks().map { CounterActivity::class }
        val progress = buttonProgress.clicks().map { ProgressActivity::class }
        val anim = buttonAnim.clicks().map { AnimActivity::class }
        val users = buttonUsers.clicks().map { UsersActivity::class }
        val photos = buttonPhotos.clicks().map { PhotosActivity::class }
        val login = buttonLogin.clicks().map { LoginActivity::class }

        Observable
            .mergeArray(hello, counter, progress, anim, users, photos, login)
            .subscribe { start(it) }
            .apply { disposables.add(this) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val github = menu.findItem(R.id.github).clicks().map { R.id.github }

        Observable
            .mergeArray(github)
            .subscribe { clickMenu(it) }
            .apply { disposables.add(this) }
    }

    private fun clickMenu(id: Int) {
        when (id) {
            R.id.github -> browse("https://github.com/importre/kotlin-maze")
        }
    }
}
