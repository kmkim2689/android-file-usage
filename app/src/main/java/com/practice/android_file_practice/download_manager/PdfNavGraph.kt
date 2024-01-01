package com.practice.android_file_practice.download_manager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.practice.android_file_practice.download_manager.data.PdfFile

@Composable
fun PdfNavGraph(
    navController: NavHostController,
    databaseReference: DatabaseReference,
    storageReference: StorageReference,
    downloadPdf: (String, String) -> Unit
) {
    NavHost(navController = navController, startDestination = "picker") {
        composable(route = "picker") {
            PdfPickerScreen(
                onListBrowseClick = {
                    navController.navigate(route = "list")
                },
                databaseReference = databaseReference,
                storageReference = storageReference
            )
        }

        composable(route = "list") {
            PdfsListScreen(
                databaseReference = databaseReference,
                storageReference = storageReference,
                onPdfItemClick = { fileName, downloadUrl ->
                    navController.navigate("view?fileName=${fileName}&downloadUrl=$downloadUrl")
                }
            )
        }

        composable(
            route = "view?fileName={fileName}&downloadUrl={downloadUrl}",
            arguments = listOf(
                navArgument("fileName") { defaultValue = "" },
                navArgument("downloadUrl") { defaultValue = "" }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                PdfContentScreen(
                    modifier = Modifier.fillMaxSize(),
                    fileName = it.getString("fileName", ""),
                    downloadUrl = it.getString("downloadUrl", ""),
                    downloadPdf = downloadPdf
                )
            }
        }
    }
}