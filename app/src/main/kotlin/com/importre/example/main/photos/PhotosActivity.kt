package com.importre.example.main.photos

import com.importre.example.base.BaseFragmentActivity

class PhotosActivity : BaseFragmentActivity<PhotosFragment>() {

    override val fragment by lazy { PhotosFragment() }

    override fun finish() {
        fragment.finish()
        super.finish()
    }
}
