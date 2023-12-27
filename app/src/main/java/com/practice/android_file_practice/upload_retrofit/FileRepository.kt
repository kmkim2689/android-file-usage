package com.practice.android_file_practice.upload_retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class FileRepository {

    suspend fun uploadImage(file: File) = try {
        FileApi.instance.uploadImage(
            image = MultipartBody.Part
                .createFormData(
                    "image",
                    file.name,
                    file.asRequestBody()
                )
        )
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } catch (e: HttpException) {
        e.printStackTrace()
        false
    }
}