package com.example.sample_camera.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder.VideoSource.CAMERA
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.URLUtil
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.ActivityChooserView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


        checkPermissions(this)

        binding.takePhotoBTN.setOnClickListener() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takeImageResult.launch(intent)


        }


    }

    private fun checkPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        }
    }

    private var takeImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
          //  doSomeOperations()
        }
    }

    companion object {
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}

