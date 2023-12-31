package com.practice.android_file_practice.download_manager.data

data class PdfFile(
    val fileName: String,
    val downloadUrl: String
) {
    constructor() : this("", "")
}// firebase needs an empty constructor to pass the data
