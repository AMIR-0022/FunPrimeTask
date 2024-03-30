package com.amar.funprime.ui.b_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amar.funprime.api.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumViewModel(private val albumRepository: AlbumRepository): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getAlbums()
        }
    }

    val albumList: LiveData<Response<List<Album>>>
        get() = albumRepository.albumList

}