package com.lordofprograms.pocketcloud.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by Михаил on 04.08.2017.
 */
@RealmClass
open class Task : RealmObject() {

    @PrimaryKey
    open var id: Long? = null

    open var task: String? = null

    open var time: String? = null

}