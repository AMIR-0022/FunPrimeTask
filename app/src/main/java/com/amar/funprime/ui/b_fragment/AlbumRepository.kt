package com.amar.funprime.ui.b_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amar.funprime.api.AlbumServices
import com.amar.funprime.api.Response

class AlbumRepository(private val albumServices: AlbumServices) {

    private val albumLiveData = MutableLiveData<Response<List<Album>>>()

    val albumList: LiveData<Response<List<Album>>>
        get() = albumLiveData

    suspend fun getAlbums(){
        try {
            val result = albumServices.getAlbums()
            if (result?.body() != null){
                albumLiveData.postValue(Response.Success(result.body()))
            }
        } catch (e: Exception){
            albumLiveData.postValue(Response.Error(e.message.toString()))
        }

    }

}
