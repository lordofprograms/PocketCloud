package com.lordofprograms.pocketcloud.mvp.views

import com.arellomobile.mvp.MvpView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import rx.Subscription

/**
 * Created by Михаил on 30.07.2017.
 */

interface LoginView : MvpView {

    fun setListeners()
    fun blockButtons(): Subscription
    fun goToTasks()

}