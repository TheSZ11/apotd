package com.szaidi.apotd.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.szaidi.apotd.R
import com.szaidi.apotd.presentation.picturedetail.PictureDetailFragment

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportActionBar?.title = getString(R.string.picture_of_the_day)

		startPictureDetailFragment()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_items, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			R.id.action_refresh -> {
				(supportFragmentManager.findFragmentById(R.id.root_fragment_container) as? PictureDetailFragment)?.fetchImage()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun startPictureDetailFragment() {
		supportFragmentManager
			.beginTransaction()
			.replace(R.id.root_fragment_container, PictureDetailFragment.newInstance(), PictureDetailFragment.TAG)
			.commit()
	}
}
