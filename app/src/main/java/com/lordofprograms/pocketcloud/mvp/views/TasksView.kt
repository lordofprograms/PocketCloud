package com.lordofprograms.pocketcloud.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.lordofprograms.pocketcloud.ui.activity.TasksActivity
import rx.Subscription

/**
 * Created by Михаил on 29.07.2017.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TasksView : MvpView {
    fun hideLoading()
    fun goToLogin()
    fun updateIcon()
    fun updateView()
    fun emptyField()
    fun onTasksLoaded()
    fun goToImages(adapter: FirebaseRecyclerAdapter<String, TasksActivity.TaskViewHolder>, position: Int, title: String)
}