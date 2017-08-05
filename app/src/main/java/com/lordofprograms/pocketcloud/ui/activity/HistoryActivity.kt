package com.lordofprograms.pocketcloud.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity

import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.db.DbService
import com.lordofprograms.pocketcloud.db.models.Task
import com.lordofprograms.pocketcloud.mvp.views.HistoryView
import com.lordofprograms.pocketcloud.ui.adapter.HistoryAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_history.*
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

class HistoryActivity : MvpAppCompatActivity(), HistoryView {

    val historyList = ArrayList<Task>()
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
        val dbService = DbService()

       return dbService.getAll(Task::class.java)
               .doOnCompleted{showEmpty()}
                .subscribe{ models ->
                    historyList += models

                    adapter.setList(historyList)
                    historyRv.adapter = adapter
                }
    }

   override fun showEmpty() {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        subs.clear()
    }
}
