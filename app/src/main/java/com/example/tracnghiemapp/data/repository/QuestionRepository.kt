package com.example.tracnghiemapp.data.repository

import android.content.Context
import com.example.tracnghiemapp.data.db.QuizDbHelper
import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.data.model.QuizHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionRepository(context: Context) {
    private val dbHelper = QuizDbHelper(context.applicationContext)

    suspend fun getAllQuestions(): List<Question> = withContext(Dispatchers.IO) {
        dbHelper.getAllQuestions()
    }

    suspend fun getQuestionById(id: Long): Question? = withContext(Dispatchers.IO) {
        dbHelper.getQuestionById(id)
    }

    suspend fun addQuestion(question: Question): Long = withContext(Dispatchers.IO) {
        dbHelper.insertQuestion(question)
    }

    suspend fun updateQuestion(question: Question): Boolean = withContext(Dispatchers.IO) {
        dbHelper.updateQuestion(question) > 0
    }

    suspend fun deleteQuestion(id: Long): Boolean = withContext(Dispatchers.IO) {
        dbHelper.deleteQuestion(id) > 0
    }

    suspend fun resetDefaultQuestions(): List<Question> = withContext(Dispatchers.IO) {
        dbHelper.resetDefaultQuestions()
        dbHelper.getAllQuestions()
    }

    suspend fun getQuestionsCount(): Int = withContext(Dispatchers.IO) {
        dbHelper.getQuestionsCount()
    }

    suspend fun saveQuizHistory(score: Int, total: Int, timeSpentSeconds: Int): Long = withContext(Dispatchers.IO) {
        dbHelper.insertHistory(score, total, timeSpentSeconds)
    }

    suspend fun getQuizHistory(): List<QuizHistory> = withContext(Dispatchers.IO) {
        dbHelper.getAllHistory()
    }
}
