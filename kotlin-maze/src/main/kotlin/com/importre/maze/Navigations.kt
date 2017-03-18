package com.importre.maze

import android.app.Activity
import android.os.Bundle
import kotlin.reflect.KClass

class Back : Navigation

class Browse(val url: Int) : Navigation

class Show(val name: String) : Navigation

class Extra<out T>(val data: T) : Navigation

class Next @JvmOverloads constructor(
    val kClass: KClass<out Activity>,
    val bundle: Bundle? = null
) : Navigation
