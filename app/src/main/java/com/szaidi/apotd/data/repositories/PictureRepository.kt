package com.szaidi.apotd.data.repositories

import com.szaidi.apotd.data.remote.ApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PictureRepository(private val apiService: ApiService = ApiService.create()) {

	fun get(date: String?): Single<Response<Any>> {
		//todo: store locally in db? return from db when api errors?

		return apiService.getImageOfTheDay(date)
			.subscribeOn(Schedulers.io())
	}
}