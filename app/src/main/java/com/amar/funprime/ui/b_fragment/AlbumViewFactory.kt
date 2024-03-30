package com.amar.funprime.ui.b_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlbumViewFactory(private val albumRepository: AlbumRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(albumRepository) as T
    }

}