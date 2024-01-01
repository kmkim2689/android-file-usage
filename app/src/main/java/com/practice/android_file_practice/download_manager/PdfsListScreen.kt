package com.practice.android_file_practice.download_manager

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.practice.android_file_practice.download_manager.data.PdfFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfsListScreen(
    modifier: Modifier = Modifier,
    databaseReference: DatabaseReference,
    storageReference: StorageReference,
    onPdfItemClick: (fileName: String, downloadUrl: String) -> Unit
) {

    /*
    * 주의
    * realtime database의 규칙을 설정하여야 데이터베이스로부터 정보를 불러올 수 있다.
    * 그렇지 않으면 "permission denied"라는 오류가 발생한다.
    * Realtime Database > Rules
    * ".read": true,
    * ".write": true로 변경
    * */

    val context = LocalContext.current

    var showProgressBar by remember {
        mutableStateOf(false)
    }

    var pdfs by rememberSaveable {
        mutableStateOf(emptyList<PdfFile>())
    }

    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val tempList = mutableListOf<PdfFile>()
            snapshot.children.forEach {
                val pdfFile = it.getValue(PdfFile::class.java)
                pdfFile?.let {
                    tempList.add(pdfFile)
                }
            }
            if (tempList.isEmpty()) {
                Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show()
            }
            pdfs = tempList
            showProgressBar = false
        }

        override fun onCancelled(error: DatabaseError) {
            // show the error
            Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
            showProgressBar = false
        }

    }

    databaseReference.addValueEventListener(valueEventListener)

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            columns = GridCells.Fixed(2)
        ) {
            items(
                items = pdfs,
                key = { pdfFile ->
                    pdfFile.fileName
                },

            ) { pdfFile ->
                PdfItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            onPdfItemClick(pdfFile.fileName, pdfFile.downloadUrl)
                        },
                    title = pdfFile.fileName
                )
            }
        }

        if (showProgressBar) {
            CircularProgressIndicator()
        }
    }
}

