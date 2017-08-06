package com.lordofprograms.pocketcloud.mvp.presenters

import android.app.Activity
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpPresenter
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.db.DbService
import com.lordofprograms.pocketcloud.db.models.Task
import com.lordofprograms.pocketcloud.mvp.views.HistoryView
import com.lordofprograms.pocketcloud.utils.toast

/**
 * Created by Михаил on 06.08.2017.
 */
class HistoryPresenter: MvpPresenter<HistoryView>() {

    fun showDeleteDialog(activity: Activity, dbService: DbService){
        MaterialDialog.Builder(activity)
                .title(R.string.delete_all)
                .content(R.string.delete_question)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive { dialog, _ ->
                    dbService.deleteAll(Task::class.java).subscribe{
                        activity.toast(R.string.history_deleted)
                        updateView(activity)
                    }
                    dialog.dismiss()
                }
                .onNegative { dialog, _ -> dialog.cancel()}
                .show()
    }

    fun updateView(activity: Activity){
        activity.finish()
        activity.overridePendingTransition(0, 0)
        activity.startActivity(activity.intent)
        activity.overridePendingTransition(0, 0)
    }


}