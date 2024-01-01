package com.practice.android_file_practice.download_manager

import android.net.Uri
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.practice.android_file_practice.download_manager.data.PdfFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfPickerScreen(
    modifier: Modifier = Modifier,
    onListBrowseClick: () -> Unit,
    storageReference: StorageReference,
    databaseReference: DatabaseReference
) {

    val context = LocalContext.current

    var pdfFileUri: Uri? by remember {
        mutableStateOf(null)
    }

    var fileName: String? by remember {
        mutableStateOf(null)
    }

    var showProgressBar by remember {
        mutableStateOf(false)
    }

    var uploadProgress by remember {
        mutableFloatStateOf(0f)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            pdfFileUri = uri
            // getting file name from uri
            fileName = uri?.let { DocumentFile.fromSingleUri(context, it)?.name }.toString()
        }
    )

    fun uploadPdfFileToFirebase() {
        val mStorageRef = storageReference.child("${System.currentTimeMillis()}/$fileName")
        pdfFileUri?.let { uri ->
            mStorageRef.putFile(uri).addOnSuccessListener {
                mStorageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val pdfFile = PdfFile(fileName.toString(), downloadUri.toString())
                    databaseReference.push().key?.let { pushKey ->
                        databaseReference.child(pushKey).setValue(pdfFile).addOnSuccessListener {
                            // 업로드가 성공되었으므로 다시 다른 파일을 선택할 수 있도록 하려면 null로 설정해야 함.
                            pdfFileUri = null
                            fileName = null
                            Toast.makeText(context, "uploaded successfully", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { error ->
                            Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnProgressListener { uploadTask ->
                showProgressBar = true

                // bytesTransferred : gives the the # of transferred (= uploaded) bytes
                uploadProgress = (uploadTask.bytesTransferred * 100 / uploadTask.totalByteCount).toFloat()
            }.addOnFailureListener { error ->
                showProgressBar = false
                Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // launch file manager to choose file from it
                launcher.launch("application/pdf")
                // to get images...
                // launcher.launch("images/*")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add pdf")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (showProgressBar && uploadProgress < 100) {
                    LinearProgressIndicator(
                        progress = uploadProgress
                    )
                }

                fileName?.let {
                    Text(text = it)
                } ?: Text(text = "choose pdf file to upload")

                Button(onClick = {
                    pdfFileUri?.let {
                        uploadPdfFileToFirebase()
                    } ?: Toast.makeText(context, "select pdf first", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "upload pdf")
                }
                Button(onClick = onListBrowseClick) {
                    Text(text = "show all")
                }
            }
        }
    }


}


/*

@Preview
@Composable
fun PdfPickerScreenPreview() {
    PdfPickerScreen(
        onListBrowseClick = {},

    )
}*/
