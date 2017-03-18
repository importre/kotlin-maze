package com.importre.example.main.photos

import com.importre.example.R
import com.importre.example.api.Api
import com.importre.example.model.Photos
import com.importre.example.utils.filterScroll
import com.importre.example.utils.shareReplay
import com.importre.maze.*
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import retrofit2.Response

private val lastPage = Regex("(?s).*?<[^>]+_page=(\\d+)[^>]*>;\\s+rel=\"last\".*")
private val pageCollection = hashSetOf<Int>()

fun photosMain(sources: Sources<PhotosModel>, api: Api): Sinks<PhotosModel> {

    val refresh = sources.event
        .refreshes(R.id.refreshLayout)
        .map { pageCollection.clear(); 0 }
        .shareReplay(1)

    val resetPage = refresh
        .withLatestFrom(sources.model,
            BiFunction { page: Int, model: PhotosModel ->
                model.copy(currentPage = page)
            }
        )

    val scroll = sources.event
        .scrollEvents(R.id.recyclerView)
        .withLatestFrom(sources.model,
            BiFunction { scroll: ScrollEvent, model: PhotosModel ->
                val event = scroll.event
                Pair(event, model)
            }
        )
        .filter { (event, model) ->
            model.currentPage <= model.totalPage && filterScroll(event)
        }
        .map { it.second.currentPage }
        .distinct({ it }, { pageCollection })
        .shareReplay(1)

    val loading = Observable
        .merge(refresh, scroll)
        .withLatestFrom(sources.model,
            BiFunction { _: Int, model: PhotosModel ->
                model.copy(loading = true)
            }
        )

    val photos = Observable
        .merge(scroll, refresh)
        .withLatestFrom(sources.model,
            BiFunction { _: Int, model: PhotosModel ->
                model.currentPage
            }
        )
        .concatMap { api.getPhotos(page = it + 1) }
        .withLatestFrom(sources.model,
            BiFunction { response: Response<Photos>, model: PhotosModel ->
                makePhotosModel(model, response)
            }
        )

    val model = Observable
        .mergeArray(resetPage, photos, loading)
        .cacheWithInitialCapacity(1)

    val back = sources.event
        .clicks(R.id.homeAsUp)
        .map { Back() }

    val photo = sources.event
        .itemSelects(R.id.recyclerView)
        .map { Extra(it.item) }

    val navigation = Observable
        .mergeArray(back, photo)

    return Sinks(model, navigation)
}

fun makePhotosModel(model: PhotosModel,
                    response: Response<Photos>): PhotosModel {
    val link = response.headers()?.get("link") ?: ""
    val totalPage = getTotalPage(link)
    return model.copy(
        photos = response.body(),
        loading = false,
        currentPage = model.currentPage + 1,
        totalPage = totalPage
    )
}

fun getTotalPage(link: String): Int {
    val totalPage = lastPage.matchEntire(link)?.let {
        it.groupValues[1].toInt(10)
    } ?: 1
    return totalPage
}
