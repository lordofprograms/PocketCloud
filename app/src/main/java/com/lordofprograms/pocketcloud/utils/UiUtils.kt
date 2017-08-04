@file:JvmName("UiUtils")

package com.lordofprograms.pocketcloud.utils

import android.content.Context
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import rx.subjects.PublishSubject
import android.widget.EditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rx.Observable


/**
 * Created by Михаил on 28.07.2017.
 */
fun Context.toast(wordRes: Int) {
    Toast.makeText(this, getString(wordRes), Toast.LENGTH_SHORT).show()
}

fun ViewGroup.inflate(resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, this, false)
}

fun getTextWatcherObservable(editText: EditText): Observable<String> {

    val subject = PublishSubject.create<String>()

    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            subject.onNext(editable.toString())
        }
    })

    return subject
}