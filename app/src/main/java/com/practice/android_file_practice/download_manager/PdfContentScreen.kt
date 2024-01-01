package com.practice.android_file_practice.download_manager

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfContentScreen(
    modifier: Modifier = Modifier,
    fileName: String,
    downloadUrl: String,
    downloadPdf: (String, String) -> Unit
) {
    Log.d("PdfContentScreen", "fileName: $fileName, downloadUrl: $downloadUrl")

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // downloadPdf(downloadUrl, fileName)
                downloadPdf(downloadUrl, fileName)
            }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "download")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
        }
    }
}

