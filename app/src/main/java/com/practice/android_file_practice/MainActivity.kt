package com.practice.android_file_practice

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.practice.android_file_practice.download_manager.PdfNavGraph
import com.practice.android_file_practice.download_manager.PdfPickerScreen
import com.practice.android_file_practice.download_manager.data.PdfFile
import com.practice.android_file_practice.ui.theme.AndroidfilepracticeTheme
import com.practice.android_file_practice.upload_retrofit.FileViewModel
import java.io.File

class MainActivity : ComponentActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storageReference = FirebaseStorage.getInstance().reference.child("pdfs")
        val databaseReference = FirebaseDatabase.getInstance().reference.child("pdfs")

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
                PdfNavGraph(
                    navController = navController,
                    databaseReference = databaseReference,
                    storageReference = storageReference
                )
            }
        }
    }



}

