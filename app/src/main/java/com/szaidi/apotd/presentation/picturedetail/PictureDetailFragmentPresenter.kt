package com.szaidi.apotd.presentation.picturedetail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver

class PictureDetailFragmentPresenter(private val view: PictureDetailFragmentContract.View,
									 private val lifecycle: Lifecycle) : PictureDetailFragmentContract.Presenter, LifecycleObserver {
	init {
		view.setPresenter(this)
		lifecycle.addObserver(this)
	}
}