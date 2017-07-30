package com.lordofprograms.pocketcloud.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

import com.google.firebase.auth.FirebaseAuth
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.utils.getTextWatcherObservable
import com.lordofprograms.pocketcloud.mvp.presenters.LoginPresenter
import com.lordofprograms.pocketcloud.mvp.views.LoginView
import com.lordofprograms.pocketcloud.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LoginActivity : MvpAppCompatActivity(), LoginView, View.OnClickListener {

    @InjectPresenter
    lateinit var presenter: LoginPresenter
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.addFirebaseListener()
        setListeners()
        blockButtons()
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.authentication -> presenter.authentication(this,auth, email.text.toString(), password.text.toString())
            R.id.registration -> presenter.registration(this, auth, email.text.toString(), password.text.toString())
        }
    }

    override fun setListeners() {
        authentication.setOnClickListener(this)
        registration.setOnClickListener(this)
    }

    override fun blockButtons(): Subscription {
        authentication.isEnabled = false
        registration.isEnabled = false

        return Observable.combineLatest(getTextWatcherObservable(email), getTextWatcherObservable(password),
                {s1, s2 ->
                    when{
                        s1.isEmpty() || s2.isEmpty() -> return@combineLatest false
                        else -> return@combineLatest true
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { call ->
                    authentication.isEnabled = call
                    registration.isEnabled = call
                }
    }

     fun taskOperating(task: Task<AuthResult>, successRes: Int, failedRes: Int) {
        when{
            task.isSuccessful -> {
                toast(successRes)
                goToTasks()
            }
            else -> toast(failedRes)
        }
    }

    override fun goToTasks(){
        finish()
        startActivity(Intent(this, TasksActivity::class.java))}

}
