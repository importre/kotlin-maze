package com.importre.example.main.photos

import android.support.v7.widget.RecyclerView
import android.view.View
import com.importre.example.model.Photo
import com.importre.example.utils.setUrl
import kotlinx.android.synthetic.main.layout_photo_item.view.*
import java.util.*

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun set(photo: Photo) {
        itemView.thumbnail.setUrl(photo.thumbnailUrl)
        itemView.title.text = photo.title
        itemView.subtitle.text = String.format(Locale.US, "id: %d", photo.id)
    }
}
