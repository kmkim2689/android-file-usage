package com.practice.android_file_practice

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val state = viewModel.state

    // for permission state
    val permissionState = rememberPermissionState(permission = READ_EXTERNAL_STORAGE)

    val context = LocalContext.current

    // add runtime permission - using effect handler
    SideEffect {
        if (!permissionState.status.isGranted) {
            // permission request 띄우기
            /*
            Request the permission to the user.
            This should always be triggered from non-composable scope,
            for example, from a side-effect or a non-composable callback. Otherwise, this will result in an IllegalStateException.
            This triggers a system dialog that asks the user to grant or revoke the permission.
            Note that this dialog might not appear on the screen
            if the user doesn't want to be asked again or has denied the permission multiple times.
            This behavior varies depending on the Android level API.
             */
            permissionState.launchPermissionRequest()
        }
    }

    // define launcher for getting the files from device
    // multiple files - getMultipleContent
    // single file - getContent
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris ->
            // get the list of uris
            // handle the callback from filePickerLauncher which returns list of uris
            viewModel.onFilePathsListChange(uris, context)
        }
    )

    // column with lazy list of path
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)) {
            // if list is empty...
            if (state.filePaths.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "pick some files")
                }
            }

            if (state.filePaths.isNotEmpty()) {
                LazyColumn {
                    items(state.filePaths) { path ->
                        Text(text = path)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // button for adding files
        Button(onClick = {
            if (permissionState.status.isGranted) {
                Log.d("MainScreen", "${permissionState.status}")
                filePickerLauncher.launch(arrayOf("*/*")) // */* mimetype : let you select all types of files
            }
            if (!permissionState.status.isGranted) {
                Log.d("MainScreen", "not granted ${permissionState.status}")
                permissionState.launchPermissionRequest()
            }
        }) {
            Text(text = "pick files")
        }
    }

}