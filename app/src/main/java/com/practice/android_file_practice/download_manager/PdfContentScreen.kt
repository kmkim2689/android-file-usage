package com.practice.android_file_practice.download_manager

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfContentScreen(
    modifier: Modifier = Modifier,
    fileName: String,
    downloadUrl: String
) {
    Log.d("PdfContentScreen", "fileName: $fileName, downloadUrl: $downloadUrl")

    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
        }
    }
}