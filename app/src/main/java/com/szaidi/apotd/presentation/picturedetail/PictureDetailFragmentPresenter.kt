package com.szaidi.apotd.presentation.picturedetail

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.gson.Gson
import com.szaidi.apotd.data.models.ApiErrorResponse
import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.repositories.PictureRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.Response

class PictureDetailFragmentPresenter(private val view: PictureDetailFragmentContract.View,
									 lifecycle: Lifecycle,
									 private val pictureRepository: PictureRepository) : PictureDetailFragmentContract.Presenter, LifecycleObserver {
	private var disposable: Disposable? = null

	override fun fetchPicture() {
		disposable = pictureRepository.get()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
					{ response: Response<Any> -> fetchPictureSuccess(response) },
					{ t: Throwable? -> t?.printStackTrace() }
			)
	}

	private fun fetchPictureSuccess(response: Response<Any>) {
		if (response.isSuccessful) {
			val responseBody = response.body() ?: return
			Gson().apply {
				view.onPictureFetched(fromJson(toJson(responseBody), PictureOfTheDay::class.java))
			}
		} else {
			val responseError = response.errorBody() ?: return
			Gson().apply {
				view.onError(fromJson(responseError.charStream(), ApiErrorResponse::class.java))
			}
		}
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