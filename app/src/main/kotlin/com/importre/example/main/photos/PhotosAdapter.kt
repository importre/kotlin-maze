package com.importre.example.main.photos

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.importre.example.R
import com.importre.example.model.Photo

class PhotosAdapter(val listener: OnItemClickListener<Photo>)
    : RecyclerView.Adapter<PhotoViewHolder>() {

    val photos: ArrayList<Photo> = arrayListOf()

    override fun getItemCount(): Int = photos.size

    fun addAll(items: Iterable<Photo>): Pair<Int, Int> {
        val start = photos.size
        val newItems = items.filter { it !in photos }
        photos.addAll(newItems)
        return Pair(start, newItems.size)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = photos[position]
            listener.onItemClick(position, item)
        }
        holder.set(photos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PhotoViewHolder {
        val layout = R.layout.layout_photo_item
        val view = View.inflate(parent.context, layout, null)
        return PhotoViewHolder(view)
    }
}
