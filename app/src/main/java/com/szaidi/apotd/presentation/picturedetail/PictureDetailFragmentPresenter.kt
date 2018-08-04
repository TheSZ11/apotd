package com.szaidi.apotd.presentation.picturedetail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.repositories.PictureRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class PictureDetailFragmentPresenter(private val view: PictureDetailFragmentContract.View,
									 lifecycle: Lifecycle,
									 private val pictureRepository: PictureRepository) : PictureDetailFragmentContract.Presenter, LifecycleObserver {
	private var disposable: Disposable? = null

	override fun fetchPicture() {
		disposable = pictureRepository.get()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
					{ picture: PictureOfTheDay -> view.onPictureFetched(picture) },
					{ t: Throwable? -> t?.printStackTrace() }
			)
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	fun onViewDetached() {
		disposable?.dispose()
	}

	init {
		view.setPresenter(this)
		lifecycle.addObserver(this)
	}
}