package com.practice.android_file_practice.upload_retrofit

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.Part

interface FileApi {

    // special function to upload file(s)
    // 다른 요청 방식과는 다른 방식을 필요로 한다. : @Multipart
    @Multipart
    // able to attach multiple different parts for the request
    // part가 될 수 있는 것들 : 단순 문자열, 파일 모두 가능 => 각각에 @Part Annotation을 붙여 활용
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    )

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("http://zipdabang.shop")
                .build()
                .create(FileApi::class.java)
        }
    }
}