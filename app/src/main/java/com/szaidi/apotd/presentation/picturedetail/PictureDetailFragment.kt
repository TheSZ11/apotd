package com.szaidi.apotd.presentation.picturedetail

import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.transition.Transition
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.szaidi.apotd.R
import com.szaidi.apotd.data.models.ApiErrorResponse
import com.szaidi.apotd.data.models.PictureOfTheDay
import com.szaidi.apotd.data.repositories.PictureRepository
import com.szaidi.apotd.presentation.MainActivity
import com.szaidi.apotd.presentation.picturefullscreen.FullScreenImageFragment.Companion.FADE_DURATION
import kotlinx.android.synthetic.main.picture_detail_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import com.bumptech.glide.load.engine.DiskCacheStrategy



class PictureDetailFragment : Fragment(), PictureDetailFragmentContract.View {
	private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
	private var presenter: PictureDetailFragmentContract.Presenter? = null
	private var picture: PictureOfTheDay? = null
	private var useHd: Boolean = false
	private val calendar: Calendar by lazy {
		Calendar.getInstance()
	}
	private val fade: Transition by lazy {
		Fade().setDuration(FADE_DURATION)
	}

	override fun setPresenter(presenter: PictureDetailFragmentContract.Presenter) {
		this.presenter = presenter
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		PictureDetailFragmentPresenter(this, lifecycle, PictureRepository())
		return inflater.inflate(R.layout.picture_detail_fragment, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		fetchImage(null)
		setOnClickListeners()
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.menu_items, menu)
	}

	override fun onPrepareOptionsMenu(menu: Menu?) {
		menu?.findItem(R.id.action_hd)?.icon =
				if (useHd) resources.getDrawable(R.drawable.ic_hd_black, null)
				else resources.getDrawable(R.drawable.ic_hd, null)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			R.id.action_refresh -> {
				calendar.get(Calendar.YEAR)
				calendar.get(Calendar.MONTH)
				calendar.get(Calendar.DAY_OF_MONTH)

				val sdf = SimpleDateFormat(getString(R.string.date_template_yyyy_MM_dd), Locale.US)
				fetchImage(sdf.format(calendar.time))
			}
			R.id.action_date -> {
				spawnDatePicker()
			}
			R.id.action_hd -> {
				useHd = !useHd
				item.icon = if (useHd) resources.getDrawable(R.drawable.ic_hd_black, null)
				else resources.getDrawable(R.drawable.ic_hd, null)
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onPictureFetched(picture: PictureOfTheDay) {
		this.picture = picture
		tv_title.text = picture.title

		Glide.with(context!!)
			.load(if (useHd) picture.hdurl else picture.url)
			.apply(RequestOptions()
				.error(R.drawable.error_image))
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

	private fun spawnDatePicker() {
		val datePickerDialog = DatePickerDialog(context, dateSetListener,
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH))
		datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
		datePickerDialog.show()
	}

	private fun fetchImage(date: String?) {
		showProgressBar()
		presenter?.fetchPicture(date)
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
				(activity as? MainActivity)?.startFullScreenImageFragment(if (useHd) picture.hdurl else picture.url)
			}
		}
		dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
			onDateSelected(year, monthOfYear, dayOfMonth)
		}
	}

	private fun onDateSelected(year: Int, monthOfYear: Int, dayOfMonth: Int) {
		calendar.set(Calendar.YEAR, year)
		calendar.set(Calendar.MONTH, monthOfYear)
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

		val sdf = SimpleDateFormat(getString(R.string.date_template_yyyy_MM_dd), Locale.US)
		fetchImage(sdf.format(calendar.time))
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