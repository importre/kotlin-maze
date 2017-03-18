package com.importre.example.dagger

import com.importre.example.main.photos.PhotosFragment
import com.importre.example.main.users.UsersFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class))
interface AppComponent {
    fun inject(f: UsersFragment)
    fun inject(f: PhotosFragment)
}
