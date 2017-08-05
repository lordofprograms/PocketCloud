package com.lordofprograms.pocketcloud.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.support.v4.app.ActivityCompat.requestPermissions
import android.util.Log
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lordofprograms.pocketcloud.R
import com.lordofprograms.pocketcloud.mvp.presenters.ImagesPresenter
import com.lordofprograms.pocketcloud.mvp.views.ImagesView
import com.lordofprograms.pocketcloud.utils.Constants
import kotlinx.android.synthetic.main.activity_images.*
import java.io.File


class ImagesActivity : MvpAppCompatActivity(), ImagesView {

    @InjectPresenter
    lateinit var presenter: ImagesPresenter
    private var imageUri = ""
    private var reference = ""
    private var tempPhoto: File? = null
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        supportActionBar?.title = getString(R.string.images)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reference = intent.getStringExtra(Constants.REFERENCE)
        addPicture.setOnClickListener{addPhoto()}
        loadFile()
    }

    override fun loadFile() {
        val localFile: File = presenter.createTempImageFile(externalCacheDir)
        storageReference.child(Constants.IMAGES + reference).getFile(localFile)
                .addOnSuccessListener {
                    Glide.with(this)
                            .load(Uri.fromFile(localFile))
                            .into(ivPicture)
                }.addOnFailureListener { exception ->
            Log.i("Load", "${exception.message}")
        }
    }

    override fun addPhoto(){
        val isCameraPermissionGranted = checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val isWritePermissionGranted = checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if(!isCameraPermissionGranted || !isWritePermissionGranted) {
            val permissions: Array<String>

            when {
                !isCameraPermissionGranted && !isWritePermissionGranted ->
                        permissions = arrayOf(android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !isCameraPermissionGranted -> permissions = arrayOf(android.Manifest.permission.CAMERA)
                else -> permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            requestPermissions(this, permissions, Constants.REQUEST_CAMERA)
        }
        else{
            tempPhoto = presenter.createTempImageFile(externalCacheDir)
            imageUri = tempPhoto!!.absolutePath

            var intentList = ArrayList<Intent>()
            var chooserIntent: Intent? = null

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            takePhotoIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempPhoto))

            intentList = presenter.addIntentToList(this, intentList, pickIntent)
            intentList = presenter.addIntentToList(this, intentList, takePhotoIntent)

            if (!intentList.isEmpty()){
                chooserIntent = Intent.createChooser(intentList.removeAt(intentList.size - 1),
                        getString(R.string.choose_source))
                chooserIntent?.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray<Parcelable>())
            }

            startActivityForResult(chooserIntent, Constants.REQUEST_TAKE_PHOTO)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.REQUEST_TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    imageUri = presenter.getRealPathFromURI(this, data.data)

                    Glide.with(baseContext)
                            .load(data.data)
                            .into(ivPicture)
                    presenter.uploadFileInFirebaseStorage(storageReference,reference,data.data)
                }
                else if (true) {
                    imageUri = Uri.fromFile(tempPhoto).toString()

                    Glide.with(this)
                            .load(imageUri)
                            .into(ivPicture)
                    presenter.uploadFileInFirebaseStorage(storageReference,reference,Uri.fromFile(tempPhoto))
                }
            }
        }
    }
}
