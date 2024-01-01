package com.practice.android_file_practice

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.practice.android_file_practice.download_manager.DownloadProgressUpdater
import com.practice.android_file_practice.download_manager.DownloadProgressUpdater.Companion.STATUS_FAILED
import com.practice.android_file_practice.download_manager.DownloadProgressUpdater.Companion.STATUS_SUCCESS
import com.practice.android_file_practice.download_manager.PdfNavGraph
import com.practice.android_file_practice.download_manager.PdfPickerScreen
import com.practice.android_file_practice.download_manager.data.PdfFile
import com.practice.android_file_practice.ui.theme.AndroidfilepracticeTheme
import com.practice.android_file_practice.upload_retrofit.FileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity(), DownloadProgressUpdater.DownloadProgressListener {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private lateinit var downloadManager: DownloadManager
    private var downloadProgressUpdater: DownloadProgressUpdater? = null
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storageReference = FirebaseStorage.getInstance().reference.child("pdfs")
        databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")

        // download manager instance
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        setContent {
            AndroidfilepracticeTheme {
                
                val navController = rememberNavController()

                /*val viewModel = viewModel<FileViewModel>()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ImagePicker()
                    Button(onClick = {
                        // 본래 파일 관련 작업은 데이터 레이어에서 이뤄져야 함
                        val file = File(cacheDir, "myImage.jpg")
                        file.createNewFile()
                        file.outputStream().use {
                            assets.open("boot.jpg").copyTo(it)
                        }
                        viewModel.uploadImage(file)
                    }) {
                        Text(text = "upload image")

                    }

                }*/


                val downloadPdf: (String, String) -> Unit = { downloadUrl, fileName ->
                    try {
                        val downloadUri = Uri.parse(downloadUrl)

                        // create request to download any file
                        val request = DownloadManager.Request(downloadUri)
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(fileName)
                            .setMimeType("application/pdf")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                File.separator + fileName
                            )

                        // downloadId : Long -> download id
                        val downloadId = downloadManager.enqueue(request)

                        // helper class -> DownloadprogressUpdater
                        // activity가 DownloadProgressUpdater의 DownloadProressListener를 상속받도록 한다.
                        downloadProgressUpdater = DownloadProgressUpdater(downloadManager, downloadId, this)
                        lifecycleScope.launch(Dispatchers.IO) {
                            downloadProgressUpdater?.run()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                PdfNavGraph(
                    navController = navController,
                    databaseReference = databaseReference,
                    storageReference = storageReference,
                    downloadPdf = downloadPdf
                )
            }
        }
    }


    override fun updateProgress(progress: Long) {
        lifecycleScope.launch(Dispatchers.Main) {
            when (progress) {
                STATUS_SUCCESS -> {
                    Toast.makeText(this@MainActivity, "download successful ${progress.toInt()}", Toast.LENGTH_SHORT).show()
                }
                STATUS_FAILED -> {
                    Toast.makeText(this@MainActivity, "download failed", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@MainActivity, "download in progress ${progress.toInt()}%", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

