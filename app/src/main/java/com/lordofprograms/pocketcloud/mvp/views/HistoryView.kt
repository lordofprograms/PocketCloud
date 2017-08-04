package com.lordofprograms.pocketcloud.mvp.views

import com.arellomobile.mvp.MvpView
import rx.Subscription

/**
 * Created by Михаил on 04.08.2017.
 */
interface HistoryView : MvpView {

    fun showEmpty()
    fun showEmptyText()
    fun hideEmptyText()
    fun loadRecyclerView(): Subscription

}