package com.szaidi.apotd.presentation.picturedetail

import com.szaidi.apotd.base.BaseView
import com.szaidi.apotd.data.models.ApiErrorResponse
import com.szaidi.apotd.data.models.PictureOfTheDay

interface PictureDetailFragmentContract {
	interface View : BaseView<Presenter> {
		fun onPictureFetched(picture: PictureOfTheDay)
		fun onError(error: ApiErrorResponse)
	}

	interface Presenter {
		fun fetchPicture()
	}
}