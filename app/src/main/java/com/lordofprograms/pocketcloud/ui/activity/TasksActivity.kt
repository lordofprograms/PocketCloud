package com.lordofprograms.pocketcloud.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.mvp.presenters.TasksPresenter
import com.lordofprograms.pocketcloud.mvp.views.TasksView
import kotlinx.android.synthetic.main.activity_tasks.*

/**
 * Created by Михаил on 29.07.2017.
 */

class TasksActivity : MvpAppCompatActivity(), TasksView {

    @InjectPresenter
    lateinit var presenter: TasksPresenter
    private val auth = FirebaseAuth.getInstance()
    private val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        presenter.checkUser(user)

        addTask.setOnClickListener { presenter.addNewTask(ref, user, "Tasks" , etTask.text.toString()) }

    }

    override fun hideLoading() {
         pb.visibility = View.GONE
    }

    override fun onTasksLoaded() {
        if(user != null) {
            rv.adapter = presenter.loadAdapter(ref, user)
            updateView()
        }
    }

    override fun updateView() {
        rv.adapter.notifyDataSetChanged()
        Log.d("Items", "${rv.adapter.itemCount} items")
    }

    override fun goToLogin() {
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun goToDetails() {
        // here will be code for opening next activty
    }

     class TaskViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val tvTask = item.findViewById(R.id.tvTask) as TextView
        val delete = item.findViewById(R.id.delete) as Button

    }

}
