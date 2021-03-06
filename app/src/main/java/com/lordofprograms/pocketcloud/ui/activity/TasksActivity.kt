package com.lordofprograms.pocketcloud.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.db.DbService
import com.lordofprograms.pocketcloud.mvp.presenters.TasksPresenter
import com.lordofprograms.pocketcloud.mvp.views.TasksView
import com.lordofprograms.pocketcloud.utils.Constants
import com.lordofprograms.pocketcloud.utils.getTextWatcherObservable
import com.lordofprograms.pocketcloud.utils.toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_tasks.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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
        Realm.init(this)

        supportActionBar?.title = getString(R.string.tasks)

        presenter.checkUser(user)

        addTask.setOnClickListener {
            presenter.addNewTask(ref, user, Constants.TASKS , etTask.text.toString())
            presenter.addNewTaskToDb(DbService(), etTask.text.toString() )
        }
    }

    override fun hideLoading() {
         pb.visibility = View.GONE
    }

    override fun onTasksLoaded() {
        if(user != null) {
            rv.adapter = presenter.loadAdapter(ref, user)
        }
    }

    override fun updateView() {
        rv.adapter.notifyDataSetChanged()
    }

     override fun updateIcon(){
           getTextWatcherObservable(etTask)
                 .map { it.length }
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                 .subscribe {
             when {
                 it > 0 -> {
                     addTask.text = getString(R.string.text_new)
                     addTask.setOnClickListener { presenter.addNewTask(ref, user, "Tasks" , etTask.text.toString()) }
                 }
                 else -> {
                     addTask.text = getString(R.string.text_photo)
                     addTask.setOnClickListener { toast(R.string.reg) }
                 }

             }
         }
    }

    override fun emptyField() {
        toast(R.string.empty_field)
    }

    override fun savedToDb() {
        toast(R.string.saved)
    }

    override fun goToLogin() {
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun goToImages(adapter: FirebaseRecyclerAdapter<String, TaskViewHolder>,position: Int, title: String) {
        val intent = Intent(this, ImagesActivity::class.java)
        intent.putExtra(Constants.REFERENCE, title)
        startActivity(intent)
    }

    override fun goToHistory() = startActivity(Intent(this, HistoryActivity::class.java))

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history -> goToHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    class TaskViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val tvTask = item.findViewById(R.id.tvTask) as TextView
        val delete = item.findViewById(R.id.delete) as Button

    }

}
