package com.example.healthlog.network

import com.example.healthlog.ui.screens.MedicalRecord
import com.example.healthlog.ui.screens.Profile
import com.example.healthlog.ui.screens.Reminder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface HealthLogApi {

    @FormUrlEncoded
    @POST("api/v1/auth/register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("new_password") newPassword: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("api/v1/profiles")
    suspend fun addProfile(
        @Field("user_id") userId: String,
        @Field("name") name: String,
        @Field("age_or_dob") ageOrDob: String,
        @Field("gender") gender: String,
        @Field("blood_group") bloodGroup: String,
        @Field("relationship") relationship: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @PUT("api/v1/profiles/{id}")
    suspend fun updateProfile(
        @Path("id") profileId: String,
        @Field("name") name: String,
        @Field("age_or_dob") ageOrDob: String,
        @Field("gender") gender: String,
        @Field("blood_group") bloodGroup: String,
        @Field("relationship") relationship: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Response<RegisterResponse>

    @GET("api/v1/profiles")
    suspend fun getProfiles(
        @Query("user_id") userId: String
    ): Response<List<Profile>>

    @DELETE("api/v1/profiles/{id}")
    suspend fun deleteProfile(
        @Path("id") profileId: String
    ): Response<RegisterResponse>

    @Multipart
    @POST("api/v1/records")
    suspend fun addMedicalRecord(
        @Part("profile_id") profileId: RequestBody,
        @Part("hospital") hospital: RequestBody,
        @Part("reason") reason: RequestBody,
        @Part("date") date: RequestBody,
        @Part("diagnosis") diagnosis: RequestBody,
        @Part report: MultipartBody.Part?
    ): Response<RegisterResponse>

    @Multipart
    @PUT("api/v1/records/{id}")
    suspend fun updateMedicalRecord(
        @Path("id") recordId: String,
        @Part("hospital") hospital: RequestBody,
        @Part("reason") reason: RequestBody,
        @Part("date") date: RequestBody,
        @Part("diagnosis") diagnosis: RequestBody,
        @Part report: MultipartBody.Part?
    ): Response<RegisterResponse>

    @GET("api/v1/records")
    suspend fun getMedicalRecords(
        @Query("profile_id") profileId: String
    ): Response<List<MedicalRecord>>

    @DELETE("api/v1/records/{id}")
    suspend fun deleteMedicalRecord(
        @Path("id") recordId: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @PUT("api/v1/users/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: String,
        @Field("gender") gender: String,
        @Field("phone") phone: String,
        @Field("blood_group") bloodGroup: String,
        @Field("age") age: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("api/v1/reminders")
    suspend fun addReminder(
        @Field("id") id: String,
        @Field("user_id") userId: String,
        @Field("title") title: String,
        @Field("type") type: String,
        @Field("time") time: String,
        @Field("date") date: String
    ): Response<RegisterResponse>

    @GET("api/v1/reminders")
    suspend fun getReminders(
        @Query("user_id") userId: String
    ): Response<List<Reminder>>

    @DELETE("api/v1/reminders/{id}")
    suspend fun deleteReminder(
        @Path("id") reminderId: String
    ): Response<RegisterResponse>

    @POST
    suspend fun getGroqCompletion(
        @Url url: String,
        @Header("Authorization") apiKey: String,
        @Body request: GroqRequest
    ): Response<GroqResponse>
}
