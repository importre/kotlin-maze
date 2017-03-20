package com.importre.example.main.photos

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.importre.example.R
import com.importre.example.api.Api
import com.importre.example.app.MazeApp
import com.importre.example.base.BaseFragment
import com.importre.example.model.Photo
import com.importre.example.utils.isPortrait
import com.importre.example.utils.toJson
import com.importre.example.utils.toast
import com.importre.maze.*
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import kotlinx.android.synthetic.main.fragment_photos.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotosFragment : BaseFragment(), MazeListener<PhotosModel>, OnItemClickListener<Photo> {

    override val layoutId: Int = R.layout.fragment_photos

    private val maze by lazy { Maze(PhotosModel()) }
    private val adapter by lazy { PhotosAdapter(this) }

    @Inject lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MazeApp.comp.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span = if (isPortrait()) 1 else 2
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, span)
        recyclerView.adapter = adapter

        maze.attach(this, arrayOf(
            toolbar.navigationClicks()
                .map { ClickEvent(R.id.homeAsUp) },
            recyclerView.scrollEvents()
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .map { ScrollEvent(R.id.recyclerView, it) },
            refreshLayout.refreshes()
                .map { RefreshEvent(R.id.refreshLayout) }
        ))

        if (savedInstanceState == null) {
            maze.event(RefreshEvent(R.id.refreshLayout))
        }
    }

    override fun onDestroyView() {
        maze.detach()
        super.onDestroyView()
    }

    override fun main(sources: Sources<PhotosModel>) = photosMain(sources, api)

    override fun render(prev: PhotosModel, curr: PhotosModel) {
        refreshLayout.isRefreshing = curr.loading
        if (curr.loading) return

        if (curr.currentPage <= 1) {
            adapter.photos.clear()
            adapter.addAll(curr.photos)
            adapter.notifyDataSetChanged()
        } else {
            val (start, length) = adapter.addAll(curr.photos)
            adapter.notifyItemRangeInserted(start, length)
        }
    }

    override fun navigate(navigation: Navigation) {
        when (navigation) {
            is Back -> activity?.onBackPressed()
            is Extra<*> -> {
                val data = navigation.data
                if (data is Photo) {
                    toast(data.toJson())
                }
            }
        }
    }

    override fun finish() = maze.finish()

    override fun onItemClick(position: Int, item: Photo) {
        val event = ItemSelectionEvent(R.id.recyclerView, position, item)
        maze.event(event)
    }
}
