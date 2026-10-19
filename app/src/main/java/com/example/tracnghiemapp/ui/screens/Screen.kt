package com.example.tracnghiemapp.ui.screens

import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.data.model.QuizResult

sealed class Screen {
    object Home : Screen()
    object ManageQuestions : Screen()
    data class AddEditQuestion(val question: Question? = null) : Screen()
    object QuizPlay : Screen()
    data class QuizResultReview(val result: QuizResult, val questions: List<Question>) : Screen()
    object History : Screen()
}
