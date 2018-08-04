package com.szaidi.apotd.presentation.picturedetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.transition.Transition
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.szaidi.apotd.R
import com.szaidi.apotd.data.models.ApiErrorResponse
import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.repositories.PictureRepository
import com.szaidi.apotd.presentation.MainActivity
import com.szaidi.apotd.presentation.picturefullscreen.FullScreenImageFragment.Companion.FADE_DURATION
import kotlinx.android.synthetic.main.picture_detail_fragment.*

class PictureDetailFragment : Fragment(), PictureDetailFragmentContract.View {
	private var presenter: PictureDetailFragmentContract.Presenter? = null
	private var picture: PictureOfTheDay? = null
	private val fade: Transition by lazy {
		Fade().setDuration(FADE_DURATION)
	}

	override fun setPresenter(presenter: PictureDetailFragmentContract.Presenter) {
		this.presenter = presenter
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		PictureDetailFragmentPresenter(this, lifecycle, PictureRepository())
		return inflater.inflate(R.layout.picture_detail_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setHasOptionsMenu(true)

		fetchImage()
		setOnClickListeners()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			R.id.action_refresh -> {
				fetchImage()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onPictureFetched(picture: PictureOfTheDay) {
		this.picture = picture
		tv_title.text = picture.title
		Glide.with(context!!)
			.load(picture.url)
			.listener(requestListener())
			.into(iv_image_of_the_day)
	}

	private fun requestListener() = object : RequestListener<Drawable> {
		override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
			hideProgressBar()
			return false
		}

		override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
			hideProgressBar()
			return false
		}
	}

	override fun onError(error: ApiErrorResponse) {
		AlertDialog.Builder(context!!).apply {
			setTitle(R.string.fetch_image_error)
			setMessage(error.error.message)
			setOnDismissListener {
				hideProgressBar()
			}
		}.create().show()
	}

	private fun fetchImage() {
		showProgressBar()
		presenter?.fetchPicture()
	}

	private fun showProgressBar() {
		(activity as? MainActivity)?.showProgressBar()
	}

	private fun hideProgressBar() {
		(activity as? MainActivity)?.hideProgressBar()
	}

	private fun setOnClickListeners() {
		iv_image_of_the_day.setOnClickListener {
			picture?.let { picture: PictureOfTheDay ->
				(activity as? MainActivity)?.startFullScreenImageFragment(picture.url)
			}
		}
	}

	companion object {
		val TAG: String = PictureDetailFragment::class.java.canonicalName
		fun newInstance(): PictureDetailFragment {
			val fragment = PictureDetailFragment()
			fragment.apply {
				exitTransition = fade
				enterTransition = fade
			}

			return fragment
		}
	}
}