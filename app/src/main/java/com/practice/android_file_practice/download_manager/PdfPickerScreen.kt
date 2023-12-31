package com.practice.android_file_practice.download_manager

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfPickerScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var pdfFileUri: Uri? by remember {
        mutableStateOf(null)
    }

    var fileName: String? by remember {
        mutableStateOf(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            pdfFileUri = uri
            // getting file name from uri
            fileName = uri?.let { DocumentFile.fromSingleUri(context, it)?.name }.toString()
        }
    )

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
                fileName?.let {
                    Text(text = it)
                } ?: Text(text = "choose pdf file to upload")
            }
        }
    }
}

@Preview
@Composable
fun PdfPickerScreenPreview() {
    PdfPickerScreen()
}