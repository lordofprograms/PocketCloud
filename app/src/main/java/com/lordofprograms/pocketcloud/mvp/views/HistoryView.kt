package com.lordofprograms.pocketcloud.mvp.views

import com.arellomobile.mvp.MvpView
import com.lordofprograms.pocketcloud.db.models.Task
import rx.Subscription
import java.util.ArrayList

/**
 * Created by Михаил on 04.08.2017.
 */
interface HistoryView : MvpView {

    fun showEmpty(historyList: ArrayList<Task>)
    fun showEmptyText()
    fun hideEmptyText()
    fun loadRecyclerView(): Subscription

}