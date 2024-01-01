package com.practice.android_file_practice.download_manager

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.util.Log
import kotlinx.coroutines.delay

class DownloadProgressUpdater(
    private val downloadManager: DownloadManager,
    private val downloadId: Long,
    private val listener: DownloadProgressListener
) {

    interface DownloadProgressListener {
        fun updateProgress(progress: Long)
    }

    private val query = DownloadManager.Query()
    private var totalBytes = 0

    init {
        query.setFilterById(downloadId)
    }

    @SuppressLint("Range")
    suspend fun run() {
        while (downloadId  > 0) {
            // to show the percentage of downloading progress every 250ms
            delay(250)
            downloadManager.query(query).use { cursor ->
                if (cursor.moveToFirst()) {
                    if (totalBytes <= 0) {
                        totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    }

                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    Log.d("Updater", "status : $status")

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            listener.updateProgress(STATUS_SUCCESS)
                            Log.d("Updater", "download success : $STATUS_SUCCESS")
                            return
                        }
                        DownloadManager.STATUS_FAILED -> {
                            listener.updateProgress(STATUS_FAILED)
                            Log.d("Updater", "download failure : $STATUS_FAILED")
                            return
                        }
                        else -> {
                            val bytesDownloadedSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            val progress = bytesDownloadedSoFar * 100L / totalBytes // percentage
                            Log.d("Updater", "$progress")

                            listener.updateProgress(progress = progress)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val STATUS_SUCCESS = 100L
        const val STATUS_FAILED = -100L
    }

}