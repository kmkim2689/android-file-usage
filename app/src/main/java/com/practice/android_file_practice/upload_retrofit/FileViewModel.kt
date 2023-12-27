package com.practice.android_file_practice.upload_retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository()
) : ViewModel() {
    fun uploadImage(file: File) {
        viewModelScope.launch {
            repository.uploadImage(file)
        }
    }
}