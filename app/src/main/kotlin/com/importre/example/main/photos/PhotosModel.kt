package com.importre.example.main.photos

import com.importre.example.model.Photos

data class PhotosModel(
    val currentPage: Int = 0,
    val totalPage: Int = Int.MAX_VALUE,
    val photos: Photos = listOf(),
    val loading: Boolean = false
)
