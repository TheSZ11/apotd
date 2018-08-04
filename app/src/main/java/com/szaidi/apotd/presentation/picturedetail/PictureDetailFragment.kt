package com.szaidi.apotd.presentation.picturedetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szaidi.apotd.R

class PictureDetailFragment : Fragment(), PictureDetailFragmentContract.View {
	private var presenter: PictureDetailFragmentContract.Presenter? = null

	override fun setPresenter(presenter: PictureDetailFragmentContract.Presenter) {
		this.presenter = presenter
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		PictureDetailFragmentPresenter(this, lifecycle)
		return inflater.inflate(R.layout.picture_detail_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}

	companion object {
		val TAG: String = PictureDetailFragment::class.java.canonicalName
		fun newInstance() = PictureDetailFragment()
	}
}