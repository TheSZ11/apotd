package com.szaidi.apotd.presentation.picturefullscreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.szaidi.apotd.R
import com.szaidi.apotd.presentation.MainActivity
import kotlinx.android.synthetic.main.full_screen_image_fragment.*

class FullScreenImageFragment : Fragment() {
	private var url: String? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		url = arguments?.getString(IMAGE_URL_EXTRA)

		return inflater.inflate(R.layout.full_screen_image_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setHasOptionsMenu(false)

		(activity as? MainActivity)?.showProgressBar()
		url?.let { loadImage(it) }
	}

	private fun loadImage(url: String) {
		Glide.with(context!!)
			.load(url)
			.into(iv_image_of_the_day)
		(activity as? MainActivity)?.hideProgressBar()
	}

	companion object {
		val TAG: String = FullScreenImageFragment::class.java.canonicalName
		const val IMAGE_URL_EXTRA = "IMAGE_URL_EXTRA"

		fun newInstance(imageUrl: String): FullScreenImageFragment {
			val fragment = FullScreenImageFragment()
			val bundle = Bundle()
			bundle.putString(IMAGE_URL_EXTRA, imageUrl)
			fragment.arguments = bundle
			return fragment
		}
	}
}