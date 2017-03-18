package com.importre.example.app

import android.app.Application
import com.importre.example.dagger.ApiModule
import com.importre.example.dagger.AppComponent
import com.importre.example.dagger.DaggerAppComponent

class MazeApp : Application() {

    companion object {
        private lateinit var me: MazeApp
        val comp by lazy { me.comp }
    }

    private lateinit var comp: AppComponent

    override fun onCreate() {
        super.onCreate()
        me = this
        comp = DaggerAppComponent.builder()
            .apiModule(ApiModule())
            .build()
    }
}
