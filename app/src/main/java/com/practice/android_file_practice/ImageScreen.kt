package com.practice.android_file_practice

import android.app.Instrumentation.ActivityResult
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    //--------------loading image-------------------
    // load image or not
    var hasImage by remember {
        mutableStateOf(false)
    }

    var imageUrl by remember {
        mutableStateOf<Uri?>(null)
    }

    // 1. define contract launcher to select content
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // 3. the returned result from the file picker : Uri
            // to display -> read the uri and convert it into a bitmap -> Coil Image Library
            hasImage = uri != null
            imageUrl = uri
        }
    )

    //----------------taking a picture----------------
    // For the camera, we first need to create a file, then make that file available to the camera so that it can write its output to it.
    // creating a file : FileProvider : specialized subclass of Content Provider(allows other apps to temporarily read/write to a file we own)
    // when opening the file picker - look for a file that "already exists"
    // get the uri

    // contract to launch the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> // output is Boolean that indicates if the action complete successfully
            hasImage = success
            // if the action is cancelled / there is an error -> receive false
        }
    )

    /*
    카메라 실행에 대한 액션을 수행하기 위하여,
    카메라에서 사진에 대한 데이터를 쓸 수 있도록 하는 File에 대한 URI가 필요하다.
    (카메라에서 사진을 찍고, 사진에 대한 데이터를 File의 URI에 write)
    이것을 위하여 FileProvider 객체를 만드는 것이 필요
    1. 먼저 프로젝트 차원에서 share하고 싶은 file들이 들어갈 경로를 설정(여기서는 image를 모아두고자 하므로, 디렉토리명으로 "image" 설정)
    => res > xml > file_paths.xml
    2. FileProvider 클래스 정의 => ComposeFileProvider.kt
    3. FileProvider 클래스를 Manifest에 정의
     */


    Box(
        modifier = modifier,
    ) {

        // if there is an image, display it
        if (hasImage && imageUrl != null) {
            AsyncImage(model = imageUrl, contentDescription = "image")
        }

        Column(
            modifier = Modifier
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Button(onClick = {
                // 2. specifying the mimetype
                imagePicker.launch("image/*")
            }) {
                Text(text = "Select Image")
            }

            Button(onClick = {
                val uri = ComposeFileProvider.getImageUri(context = context)
                imageUrl = uri
                cameraLauncher.launch(uri)
            }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Take photo")
            }
        }
    }
}