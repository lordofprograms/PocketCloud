package com.lordofprograms.pocketcloud.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.ui.activity.TasksActivity
import com.lordofprograms.pocketcloud.mvp.views.TasksView

/**
 * Created by Михаил on 29.07.2017.
 */
@InjectViewState
class TasksPresenter : MvpPresenter<TasksView>(){


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.onTasksLoaded()
    }

    fun addNewTask(ref: DatabaseReference, user: FirebaseUser?, secondChild: String, valueText: String) {
        ref.child(user?.uid).child(secondChild).push().setValue(valueText)
    }

    fun checkUser(user: FirebaseUser?){
        if (user?.email == null) {
            viewState.goToLogin()
        }
    }

    fun loadAdapter(ref: DatabaseReference, user: FirebaseUser?):
            FirebaseRecyclerAdapter<String, TasksActivity.TaskViewHolder> {
        return  object : FirebaseRecyclerAdapter<String, TasksActivity.TaskViewHolder>(
                String::class.java, R.layout.item_task,
                TasksActivity.TaskViewHolder::class.java, ref.child(user?.uid).child("Tasks")) {

            override fun onDataChanged() {
                super.onDataChanged()
                viewState.hideLoading()
            }

            override fun populateViewHolder(viewHolder: TasksActivity.TaskViewHolder, title: String, position: Int) {
                viewHolder.tvTask.text = title
                viewHolder.delete.setOnClickListener {
                    val itemRef = getRef(position)
                    itemRef.removeValue()
                    viewState.updateView()
                }

                viewHolder.itemView.setOnClickListener { viewState.goToDetails() }
            }
        }
    }

}