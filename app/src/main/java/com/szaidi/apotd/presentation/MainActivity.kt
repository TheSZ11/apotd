package com.szaidi.apotd.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.szaidi.apotd.R
import com.szaidi.apotd.presentation.picturedetail.PictureDetailFragment

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportActionBar?.title = getString(R.string.picture_of_the_day)

		startPictureDetailFragment()
	}

	private fun startPictureDetailFragment() {
		supportFragmentManager
			.beginTransaction()
			.replace(R.id.root_fragment_container, PictureDetailFragment.newInstance(), PictureDetailFragment.TAG)
			.commit()
	}
}
