package com.amar.funprime.api

import com.amar.funprime.ui.b_fragment.Album
import retrofit2.Response
import retrofit2.http.GET

interface AlbumServices {

    @GET("/photos")
    suspend fun getAlbums() : Response<List<Album>>

}