package com.lordofprograms.pocketcloud.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.db.DbService
import com.lordofprograms.pocketcloud.db.models.Task
import com.lordofprograms.pocketcloud.mvp.presenters.HistoryPresenter
import com.lordofprograms.pocketcloud.mvp.views.HistoryView
import com.lordofprograms.pocketcloud.ui.adapter.HistoryAdapter
import com.lordofprograms.pocketcloud.utils.toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_history.*
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

class HistoryActivity : MvpAppCompatActivity(), HistoryView {

    @InjectPresenter
    lateinit var presenter: HistoryPresenter
    val historyList = ArrayList<Task>()
    val dbService = DbService()
    val subs = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        Realm.init(this)

        supportActionBar?.title = getString(R.string.history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subs.add(loadRecyclerView())
    }

    override fun loadRecyclerView(): Subscription {
        val adapter = HistoryAdapter()

       return dbService.getAll(Task::class.java)
               .doOnCompleted{showEmpty(historyList)}
                .subscribe{ models ->
                    historyList += models

                    adapter.setList(historyList)
                    historyRv.adapter = adapter
                }
    }

    override fun showEmpty(historyList: ArrayList<Task>) {
        when {
            historyList.isEmpty() -> showEmptyText()
            historyList.isNotEmpty() -> hideEmptyText()
        }
    }

    override fun showEmptyText() {
        historyRv.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
    }

    override fun hideEmptyText() {
        historyRv.visibility = View.VISIBLE
        emptyText.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> super.onBackPressed()
            R.id.delete_all -> {
                presenter.showDeleteDialog(this, dbService)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        subs.clear()
    }
}
