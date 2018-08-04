package com.szaidi.apotd.presentation.picturedetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.szaidi.apotd.R
import com.szaidi.apotd.data.models.ApiErrorResponse
import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.repositories.PictureRepository
import kotlinx.android.synthetic.main.picture_detail_fragment.*
import kotlinx.android.synthetic.main.progress_view.*

class PictureDetailFragment : Fragment(), PictureDetailFragmentContract.View {
	private var presenter: PictureDetailFragmentContract.Presenter? = null

	override fun setPresenter(presenter: PictureDetailFragmentContract.Presenter) {
		this.presenter = presenter
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		PictureDetailFragmentPresenter(this, lifecycle, PictureRepository())
		return inflater.inflate(R.layout.picture_detail_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		fetchImage()
	}

	override fun onPictureFetched(picture: PictureOfTheDay) {
		tv_title.text = picture.title
		tv_exlanation.text = picture.explanation
		Glide.with(context!!)
			.load(picture.url)
			.into(iv_image_of_the_day)
		progress_view.visibility = View.GONE
	}

	override fun onError(error: ApiErrorResponse) {
		AlertDialog.Builder(context!!).apply {
			setTitle(R.string.fetch_image_error)
			setMessage(error.error.message)
			setOnDismissListener {
				progress_view.visibility = View.GONE
			}
		}.create().show()
	}

	fun fetchImage(){
		progress_view.visibility = View.VISIBLE
		presenter?.fetchPicture()
	}

	companion object {
		val TAG: String = PictureDetailFragment::class.java.canonicalName
		fun newInstance() = PictureDetailFragment()
	}
}