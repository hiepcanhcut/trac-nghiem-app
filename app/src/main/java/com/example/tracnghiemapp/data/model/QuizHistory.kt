package com.example.tracnghiemapp.data.model

data class QuizHistory(
    val id: Long = 0,
    val dateEpochMillis: Long,
    val score: Int,
    val totalQuestions: Int,
    val timeSpentSeconds: Int
) {
    val percentage: Int
        get() = if (totalQuestions > 0) (score * 100) / totalQuestions else 0
}
