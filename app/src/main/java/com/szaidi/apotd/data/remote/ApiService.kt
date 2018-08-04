package com.szaidi.apotd.data.remote

import com.szaidi.apotd.data.models.PictureOfTheDay
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
	companion object {
		private const val BASE_URL = "https://api.nasa.gov"
		const val API_KEY = "NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo"

		fun create(): ApiService {
			val retrofit = Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(BASE_URL)
				.build()

			return retrofit.create(ApiService::class.java)
		}
	}

	@GET("/planetary/apod?api_key=$API_KEY")
	fun getImageOfTheDay(): Single<Response<Any>>
}