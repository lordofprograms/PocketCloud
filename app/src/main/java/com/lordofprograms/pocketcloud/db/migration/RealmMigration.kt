package com.lordofprograms.pocketcloud.db.migration

import com.lordofprograms.pocketcloud.utils.Constants
import io.realm.DynamicRealm

/**
 * Created by Михаил on 04.08.2017.
 */
class RealmMigration : io.realm.RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
       realm.schema.create(Constants.TASK_MODEL)
                .addField("task", String::class.java)
                .addField("time", String::class.java)
    }

    override fun hashCode(): Int {
        return com.lordofprograms.pocketcloud.db.migration.RealmMigration::class.java.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is com.lordofprograms.pocketcloud.db.migration.RealmMigration
    }
}