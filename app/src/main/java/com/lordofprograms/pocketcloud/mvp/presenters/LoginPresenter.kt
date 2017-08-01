package com.lordofprograms.pocketcloud.mvp.presenters

import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.auth.FirebaseAuth
import com.lordofprograms.pocketcloud.ui.activity.LoginActivity
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.mvp.views.LoginView
import rx.subscriptions.CompositeSubscription

/**
 * Created by Михаил on 30.07.2017.
 */
class LoginPresenter : MvpPresenter<LoginView>() {

    fun addFirebaseListener(): FirebaseAuth.AuthStateListener{
        return FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                viewState.goToTasks()
            }
        }
    }

    fun authentication(activity: LoginActivity, auth: FirebaseAuth, email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            activity.taskOperating(task, R.string.auth_succeed, R.string.auth_failed)
        }
    }

    fun registration(activity: LoginActivity, auth: FirebaseAuth, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            activity.taskOperating(task, R.string.reg_succeed, R.string.reg_failed)
        }
    }

}