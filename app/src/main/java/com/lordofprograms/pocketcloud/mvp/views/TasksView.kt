package com.lordofprograms.pocketcloud.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by Михаил on 29.07.2017.
 */
@StateStrategyType(value = AddToEndSingleStrategy::class)
interface TasksView : MvpView {
    fun hideLoading()
    fun goToLogin()
    fun updateView()
    fun onTasksLoaded()
    fun goToDetails()
}