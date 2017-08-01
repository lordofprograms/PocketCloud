package com.lordofprograms.pocketcloud.mvp.presenters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.storage.StorageReference
import com.lordofprograms.pocketcloud.mvp.views.ImagesView
import com.lordofprograms.pocketcloud.utils.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Михаил on 01.08.2017.
 */
class ImagesPresenter : MvpPresenter<ImagesView>() {

    fun getRealPathFromURI(activity: Activity, uri: Uri): String{
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        @Suppress("DEPRECATION")
        val cursor = activity.managedQuery(uri, projection, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    fun createTempImageFile(storageDir: File): File {
        val timeStamp = SimpleDateFormat("yyyyMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "photo_$timeStamp"

        return File.createTempFile(
                imageFileName,    //prefix
                ".jpg",           //suffix
                storageDir        //directory

        )
    }

    fun uploadFileInFirebaseStorage(storageReference: StorageReference,reference: String, uri: Uri){
        val uploadTask = storageReference.child(Constants.IMAGES + reference).putFile(uri)
        uploadTask.addOnProgressListener{
            taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred)
            Log.i("Load", "Upload is $progress% done")
        }.addOnSuccessListener {
            taskSnapshot ->
            val downloadUrl = taskSnapshot.metadata?.downloadUrl
            Log.i("Load" , "Url donwload is $downloadUrl")
        }
    }

    fun addIntentToList(context: Context, intentList: ArrayList<Intent>, intent: Intent): ArrayList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.`package` = packageName
            intentList.add(targetedIntent)
        }
        return intentList
    }


}