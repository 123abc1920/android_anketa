package com.example.task1.controller.service

import com.example.task1.controller.models.Answer
import com.example.task1.controller.models.Question
import com.example.task1.controller.models.Quiz
import com.example.task1.controller.models.User
import com.example.task1.controller.models.Result
import com.example.task1.controller.models.UserAnswer

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("anketas")
    suspend fun getQuizzes(): List<Quiz>
}


/*
*  // Users
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @POST("users")
    suspend fun createUser(@Body user: User): User

    // Quizzes
    @GET("anketas")
    suspend fun getQuizzes(): List<Quiz>

    @GET("quizzes/{id}")
    suspend fun getQuizById(@Path("id") id: Int): Quiz

    @GET("quizzes/active")
    suspend fun getActiveQuizzes(): List<Quiz>

    // Results
    @GET("results")
    suspend fun getResults(): List<Result>

    @GET("results/user/{userId}")
    suspend fun getUserResults(@Path("userId") userId: Int): List<Result>

    @GET("results/quiz/{quizId}")
    suspend fun getQuizResults(@Path("quizId") quizId: Int): List<Result>

    @POST("results")
    suspend fun createResult(@Body result: Result): Result

    // Questions
    @GET("questions")
    suspend fun getQuestions(): List<Question>

    @GET("questions/quiz/{quizId}")
    suspend fun getQuizQuestions(@Path("quizId") quizId: Int): List<Question>

    // Answers
    @GET("answers")
    suspend fun getAnswers(): List<Answer>

    @GET("answers/question/{questionId}")
    suspend fun getQuestionAnswers(@Path("questionId") questionId: Int): List<Answer>

    // User Answers
    @GET("user-answers")
    suspend fun getUserAnswers(): List<UserAnswer>

    @GET("user-answers/result/{resultId}")
    suspend fun getResultAnswers(@Path("resultId") resultId: Int): List<UserAnswer>

    @POST("user-answers")
    suspend fun createUserAnswer(@Body userAnswer: UserAnswer): UserAnswer

    @POST("user-answers/batch")
    suspend fun createUserAnswers(@Body userAnswers: List<UserAnswer>): List<UserAnswer>*/