package com.szaidi.apotd.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import com.szaidi.apotd.R
import com.szaidi.apotd.presentation.picturedetail.PictureDetailFragment
import com.szaidi.apotd.presentation.picturefullscreen.FullScreenImageFragment
import kotlinx.android.synthetic.main.progress_view.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportActionBar?.title = getString(R.string.picture_of_the_day)

		addOnBackStackListener()
		startPictureDetailFragment()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_items, menu)
		return false
	}

	private fun addOnBackStackListener() {
		supportFragmentManager.addOnBackStackChangedListener {
			val currentFragment = supportFragmentManager.findFragmentById(R.id.root_fragment_container)
			when (currentFragment) {
				is FullScreenImageFragment -> {
					supportActionBar?.apply {
						title = null
						hide()
					}
					window.decorView.apply {
						systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE
						systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
						systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					}
				}
				is PictureDetailFragment -> {
					supportActionBar?.apply {
						title = getString(R.string.picture_of_the_day)
						show()
					}
				}
			}
		}
	}

	private fun startPictureDetailFragment() {
		supportFragmentManager
			.beginTransaction()
			.replace(R.id.root_fragment_container, PictureDetailFragment.newInstance(), PictureDetailFragment.TAG)
			.commit()
	}

	fun startFullScreenImageFragment(url: String) {
		supportFragmentManager
			.beginTransaction()
			.replace(R.id.root_fragment_container, FullScreenImageFragment.newInstance(url), FullScreenImageFragment.TAG)
			.addToBackStack(FullScreenImageFragment.TAG)
			.commit()
	}

	fun showProgressBar() {
		progress_view.visibility = View.VISIBLE
	}

	fun hideProgressBar() {
		progress_view.visibility = View.GONE
	}
}
