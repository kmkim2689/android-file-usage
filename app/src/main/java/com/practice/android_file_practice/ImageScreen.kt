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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
) {

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

            Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Take photo")
            }
        }
    }
}