package com.szaidi.apotd.base

interface BaseView<T> {
	fun setPresenter(presenter: T)
}