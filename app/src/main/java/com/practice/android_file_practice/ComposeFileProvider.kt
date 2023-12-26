package com.practice.android_file_practice

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.file_paths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            // We get the path to the directory where we want the file to be stored.
            // this has to match one of the paths defined in our filepaths.xml file we described earlier.
            val directory = File(context.cacheDir, "images") // file_paths.xml 파일에 images라는 폴더로 정의되어 있으므로...
            // We create a temporary file in this directory.
            directory.mkdirs()

            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )

            //We get the authority for our content provider (which has to match the one we defined in the manifest).
            // in manifest : android:authorities="com.practice.android_file_practice.fileprovider"
            val authority = context.packageName + ".fileprovider"

            return getUriForFile(
                context, authority, file
            )
        }
    }
}

/*
* we define a set of folders where we want to store the files to share
* (we can define different paths based on the type of folder we want to use,
* like files or cache),
* we subclass FileProvider and we provide the files paths we defined earlier,
* and finally we add a provider section to the manifest with our authority
* */