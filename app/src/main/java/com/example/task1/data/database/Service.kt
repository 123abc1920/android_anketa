package com.example.task1.data.database

import com.example.task1.data.database.responses.AuthResponse
import com.example.task1.data.api.models.Quiz
import com.example.task1.data.database.requests.LoginRequest
import com.example.task1.data.database.requests.QuizRequest
import com.example.task1.data.database.responses.QuizDataResponse
import com.example.task1.data.database.responses.QuizStatisticsResponse
import com.example.task1.data.database.responses.ResultResponse
import com.example.task1.data.database.responses.UserDataResponse

import retrofit2.http.*

interface ApiService {

    @GET("anketas")
    suspend fun getQuizzes(): List<Quiz>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("signup")
    suspend fun signup(@Body loginRequest: LoginRequest): AuthResponse

    @GET("get_user_data")
    suspend fun getUserData(@Header("Authorization") id: String): UserDataResponse

    @GET("/start/anketa")
    suspend fun startAnketa(@Query("id") quizId: String): QuizDataResponse

    @POST("/get/quiz")
    suspend fun sendQuiz(@Header("Authorization") id: String, @Body requestData: QuizRequest): ResultResponse

    @GET("/start/preview")
    suspend fun startPreview(@Query("id") quizId: String): QuizStatisticsResponse
}