package com.example.tracnghiemapp.data.model

data class Question(
    val id: Long = 0,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0 for A, 1 for B, 2 for C, 3 for D
    val explanation: String = "",
    val category: String = "Tổng hợp"
) {
    fun getOptions(): List<String> = listOf(optionA, optionB, optionC, optionD)

    fun getCorrectOptionLetter(): String = when (correctOptionIndex) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        else -> "A"
    }

    fun getCorrectOptionText(): String = when (correctOptionIndex) {
        0 -> optionA
        1 -> optionB
        2 -> optionC
        3 -> optionD
        else -> optionA
    }
}

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeSpentSeconds: Int,
    val userAnswers: Map<Int, Int> // questionIndex -> selectedOptionIndex (0..3, or -1 if skipped)
) {
    val percentage: Int
        get() = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
}
