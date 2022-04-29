package com.xxx.xxxmusic.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xxx.xxxmusic.R

class PageViewModel : ViewModel() {

    private val _list_ = MutableLiveData<List<Int>>().apply {
        value = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9,
            R.drawable.image10,
        ).shuffled()
    }
    val list: LiveData<List<Int>> = _list_

}