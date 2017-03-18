package com.importre.example.main.photos

interface OnItemClickListener<in T> {
    fun onItemClick(position: Int, item: T)
}
