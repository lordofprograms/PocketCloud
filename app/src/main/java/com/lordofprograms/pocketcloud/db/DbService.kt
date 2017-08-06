package com.lordofprograms.pocketcloud.db

import android.util.Log
import com.lordofprograms.pocketcloud.db.migration.RealmMigration
import com.lordofprograms.pocketcloud.db.models.Task
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by Михаил on 04.08.2017.
 */
class DbService {

    private val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration(RealmMigration())
            .build()


    fun <T : RealmObject> save(`object`: T, clazz: Class<T>): Observable<RealmObject> {
        val realm = Realm.getInstance(config)

        var id: Long

        try {
            id = (realm.where(clazz).max("id").toInt() + 1).toLong()
        } catch (e: Exception) {
            id = 0L
        }

        (`object` as Task).id = id

        return Observable.just(`object`)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { t ->
                    Observable.just(t)
                            .doOnSubscribe(realm::beginTransaction)
                            .doOnUnsubscribe {
                                realm.commitTransaction()
                                realm.close()
                            }
                            .doOnNext { realm.copyToRealm(it) }
                }
    }

    fun <T : RealmObject> getAll(clazz: Class<T>): Observable<List<T>> {
        val realm = Realm.getInstance(config)

        return Observable.just(clazz)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { t ->
                    Observable.just(t)
                            .doOnSubscribe(realm::beginTransaction)
                            .doOnUnsubscribe {
                                realm.commitTransaction()
                            }
                            .map { type -> realm.where(type).findAll() }
                }
    }

    fun <T : RealmObject> delete(clazz: Class<T>, id: Int): Observable<RealmObject> {
        val realm = Realm.getInstance(config)

        return realm.where(clazz).equalTo("id", id).findAllAsync().asObservable()
                /*.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())*/
                .flatMap { tasks -> Observable.from<RealmObject>(tasks) }
                .doOnError { error -> Log.e("Delete", error.message) }
                .doOnSubscribe{realm.beginTransaction()}
                .doOnUnsubscribe {
                    realm.commitTransaction()
                    realm.close()
                }
    }

    fun <T : RealmObject> deleteAll(clazz: Class<T>): Observable<Boolean> {
        val realm = Realm.getInstance(config)

        return Observable.just(clazz)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { t ->
                    Observable.just(t)
                            .doOnSubscribe { realm.beginTransaction() }
                            .doOnUnsubscribe {
                                realm.commitTransaction()
                                realm.close()
                            }
                            .map { type -> realm.where(type).findAll().deleteAllFromRealm() }
                }
    }

}