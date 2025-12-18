package com.example.task1.data.database

import com.example.task1.data.database.responses.AuthResponse
import com.example.task1.data.database.requests.LoginRequest
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.data.database.requests.SearchQuizRequest
import com.example.task1.data.database.responses.CreateQuizResponse
import com.example.task1.data.database.responses.EditQuizRequest
import com.example.task1.data.database.responses.QuizDataForEditResponse
import com.example.task1.data.database.responses.QuizDataResponse
import com.example.task1.data.database.responses.QuizStatisticsResponse
import com.example.task1.data.database.responses.QuizzesResponse
import com.example.task1.data.database.responses.ResultResponse
import com.example.task1.data.database.responses.UserDataResponse

import retrofit2.http.*

interface ApiService {

    @GET("anketas")
    suspend fun getQuizzes(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): QuizzesResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("signup")
    suspend fun signup(@Body loginRequest: LoginRequest): AuthResponse

    @GET("get_user_data")
    suspend fun getUserData(@Header("Authorization") id: String): UserDataResponse

    @GET("start/anketa")
    suspend fun startAnketa(
        @Header("Authorization") id: String,
        @Query("id") quizId: String
    ): QuizDataResponse

    @POST("delete/quiz")
    suspend fun deleteQuiz(@Body request: Map<String, Int?>): ResultResponse

    @POST("create/quiz")
    suspend fun createQuiz(
        @Header("Authorization") id: String,
        @Body quizData: CreateQuizResponse
    ): ResultResponse

    @POST("get/quiz")
    suspend fun sendQuiz(
        @Header("Authorization") id: String,
        @Body requestData: QuizRequest
    ): ResultResponse

    @GET("start/preview")
    suspend fun startPreview(@Query("id") quizId: String): QuizStatisticsResponse

    @POST("set/data")
    suspend fun setData(
        @Header("Authorization") id: String,
        @Body request: Map<String, String>
    ): ResultResponse

    @POST("change/password")
    suspend fun setPassword(
        @Header("Authorization") id: String,
        @Body request: Map<String, String>
    ): ResultResponse

    @GET("get/quiz/data")
    suspend fun getQuizData(
        @Header("Authorization") id: String,
        @Query("id") quizId: String
    ): QuizDataForEditResponse

    @POST("edit/quiz")
    suspend fun editQuiz(
        @Body data: EditQuizRequest
    ): ResultResponse

    @POST("search")
    suspend fun search(
        @Body data: SearchQuizRequest
    ): QuizzesResponse
}