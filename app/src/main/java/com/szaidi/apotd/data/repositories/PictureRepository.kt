package com.szaidi.apotd.data.repositories

import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.remote.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class PictureRepository(private val apiService: ApiService = ApiService.create()) {

	fun get(): Observable<PictureOfTheDay> {
		//todo: store locally in db? return from db when api errors?

		return apiService.getImageOfTheDay()
			.subscribeOn(Schedulers.io())
			.toObservable()
	}
}