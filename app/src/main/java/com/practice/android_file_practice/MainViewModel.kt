package com.practice.android_file_practice

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    // get reference to the UriPathFinder class
    private val uriPathFinder = UriPathFinder()

    // handle the list of uris
    fun onFilePathsListChange(uris: List<Uri>, context: Context) {
        val updatedList = state.filePaths.toMutableList()
        // store the previous list
        val paths = changeUriToPath(uris, context)
        viewModelScope.launch {
            updatedList += paths
            state = state.copy(
                filePaths = updatedList
            )
        }

    }

    private fun changeUriToPath(uris: List<Uri>, context: Context): List<String> {
        // create mutable list
        val pathList: MutableList<String> = mutableListOf()
        // iterate over all the uris and get path
        uris.forEach {
            val path = uriPathFinder.getPath(context, it)
            pathList.add(path!!)
        }
        return pathList
    }

}