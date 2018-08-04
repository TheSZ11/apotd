package com.szaidi.apotd.presentation.picturefullscreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.Fade
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.szaidi.apotd.R
import com.szaidi.apotd.presentation.MainActivity
import kotlinx.android.synthetic.main.full_screen_image_fragment.*

class FullScreenImageFragment : Fragment() {
	private var url: String? = null
	private val fade: Transition by lazy {
		Fade().setDuration(FADE_DURATION)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		url = arguments?.getString(IMAGE_URL_EXTRA)

		return inflater.inflate(R.layout.full_screen_image_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setHasOptionsMenu(false)

		(activity as? MainActivity)?.showProgressBar()
		url?.let { loadImage(it) }
		setUpClickListeners()
	}

	private fun setUpClickListeners() {
		iv_image_of_the_day.setOnClickListener {
			(activity as? MainActivity)?.onBackPressed()
		}
	}

	private fun loadImage(url: String) {
		Glide.with(context!!)
			.load(url)
			.listener(requestListener())
			.into(iv_image_of_the_day)
	}

	private fun requestListener() = object : RequestListener<Drawable> {
		override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
			(activity as? MainActivity)?.hideProgressBar()
			return false
		}

		override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
			(activity as? MainActivity)?.hideProgressBar()
			return false
		}

	}

	companion object {
		val TAG: String = FullScreenImageFragment::class.java.canonicalName
		const val IMAGE_URL_EXTRA = "IMAGE_URL_EXTRA"
		const val FADE_DURATION: Long = 350

		fun newInstance(imageUrl: String): FullScreenImageFragment {
			val fragment = FullScreenImageFragment()
			val bundle = Bundle()
			bundle.putString(IMAGE_URL_EXTRA, imageUrl)
			fragment.apply {
				arguments = bundle
				enterTransition = fade
				exitTransition = fade
			}
			return fragment
		}
	}
}