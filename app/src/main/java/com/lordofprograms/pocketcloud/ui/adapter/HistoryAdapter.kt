package com.lordofprograms.pocketcloud.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.db.DbService
import com.lordofprograms.pocketcloud.db.models.Task
import com.lordofprograms.pocketcloud.utils.inflate
import io.realm.Realm

/**
 * Created by Михаил on 04.08.2017.
 */
class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    private var historyList: ArrayList<Task> = ArrayList()

    fun setList(list: ArrayList<Task>){
        this.historyList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = parent.inflate(R.layout.item_history)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.time.text = historyList[position].time
        holder.task.text = historyList[position].task
       /* holder.delete.setOnClickListener{
            DbService().delete(Task::class.java, position).subscribe{ tasks -> tasks.deleteFromRealm()}
            notifyDataSetChanged()
        }*/
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class HistoryItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val time = view.findViewById(R.id.taskTime) as TextView
        val task = view.findViewById(R.id.taskName) as TextView
        val delete = view.findViewById(R.id.delete) as ImageView
    }

}