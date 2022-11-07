package com.example.sample_camera.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder.VideoSource.CAMERA
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.AndroidException
import android.webkit.URLUtil
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ActivityChooserView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.sample_camera.BuildConfig

import com.example.sample_camera.R
import com.example.sample_camera.databinding.CameraActivityBinding
import java.io.File
import java.util.jar.Manifest

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: CameraActivityBinding
    private var latestTmpUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CameraActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermission()

        val takeImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                }
            }

        binding.takePhotoBTN.setOnClickListener() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takeImageResult.launch(intent)
        }
    }

    private fun checkAndRequestPermission(): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val listPermissionNeeded = ArrayList<String>()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        } else return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms[android.Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[android.Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED) {
                        val i = Intent(this@CameraActivity, CameraActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                android.Manifest.permission.CAMERA
                            )
                        ) {
                            showDialogOK(
                                "Service Permissions are required for this app",
                                DialogInterface.OnClickListener { _, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermission()
                                        DialogInterface.BUTTON_NEGATIVE -> finish()
                                        DialogInterface.BUTTON_NEUTRAL -> finish()
                                    }

                                })
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                        }
                    }
                }
            }
        }

    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

    private fun explain(msg: String) {
        val dialog = AlertDialog.Builder(this)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + baseContext.packageName)
        )
        dialog
            .setMessage(msg)
            .setPositiveButton("OK") { _, _ -> startActivity(intent) }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }

    companion object {
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }

    enum class PermissionResult { GRANTED, REJECTED, PERMANENT_REFUSAL }

}

/*
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        CameraActivity.REQUEST_ID_MULTIPLE_PERMISSIONS -> {
            val perms = HashMap<String, Int>()
            perms[android.Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]
                if (perms[android.Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED) {
                    val i = Intent(this@CameraActivity, CameraActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            android.Manifest.permission.CAMERA
                        )
                    ) {
                        showDialogOK(
                            "Service Permissions are required for this app",
                            DialogInterface.OnClickListener { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermission()
                                    DialogInterface.BUTTON_NEGATIVE -> finish()
                                    DialogInterface.BUTTON_NEUTRAL -> finish()
                                }

                            })
                    } else {
                        explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                    }
                }
            }
        }
    }

}

*/
