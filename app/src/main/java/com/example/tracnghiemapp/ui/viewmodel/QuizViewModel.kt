package com.example.tracnghiemapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.data.model.QuizHistory
import com.example.tracnghiemapp.data.model.QuizResult
import com.example.tracnghiemapp.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val filteredQuestions: List<Question> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "Tất cả",
    val historyList: List<QuizHistory> = emptyList(),
    val isLoading: Boolean = false,

    // Quiz Play State
    val quizQuestions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> chosenOption (0..3)
    val flaggedQuestionIndices: Set<Int> = emptySet(),
    val elapsedSeconds: Int = 0,
    val isQuizActive: Boolean = false,
    val lastResult: QuizResult? = null,

    // Add / Edit State
    val editingQuestion: Question? = null,
    val userMessage: String? = null
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionRepository(application)

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
        loadHistory()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = repository.getAllQuestions()
            _uiState.update {
                it.copy(
                    questions = list,
                    filteredQuestions = filterQuestionsList(list, it.searchQuery, it.selectedCategory),
                    isLoading = false
                )
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            val history = repository.getQuizHistory()
            _uiState.update { it.copy(historyList = history) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredQuestions = filterQuestionsList(it.questions, query, it.selectedCategory)
            )
        }
    }

    fun onCategoryFilterChanged(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                filteredQuestions = filterQuestionsList(it.questions, it.searchQuery, category)
            )
        }
    }

    private fun filterQuestionsList(list: List<Question>, query: String, category: String): List<Question> {
        return list.filter { q ->
            val matchesQuery = query.isBlank() ||
                    q.questionText.contains(query, ignoreCase = true) ||
                    q.optionA.contains(query, ignoreCase = true) ||
                    q.optionB.contains(query, ignoreCase = true) ||
                    q.optionC.contains(query, ignoreCase = true) ||
                    q.optionD.contains(query, ignoreCase = true)
            val matchesCategory = category == "Tất cả" || q.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }

    fun getCategories(): List<String> {
        val categories = _uiState.value.questions.map { it.category.trim() }.filter { it.isNotBlank() }.distinct()
        return listOf("Tất cả") + categories
    }

    fun saveQuestion(
        id: Long,
        questionText: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctIndex: Int,
        explanation: String,
        category: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val trimmedCategory = if (category.isBlank()) "Tổng hợp" else category.trim()
            val question = Question(
                id = id,
                questionText = questionText.trim(),
                optionA = optionA.trim(),
                optionB = optionB.trim(),
                optionC = optionC.trim(),
                optionD = optionD.trim(),
                correctOptionIndex = correctIndex,
                explanation = explanation.trim(),
                category = trimmedCategory
            )

            if (id == 0L) {
                repository.addQuestion(question)
                _uiState.update { it.copy(userMessage = "Đã thêm câu hỏi mới thành công!") }
            } else {
                repository.updateQuestion(question)
                _uiState.update { it.copy(userMessage = "Đã cập nhật câu hỏi thành công!") }
            }
            loadQuestions()
            onSuccess()
        }
    }

    fun deleteQuestion(id: Long) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
            _uiState.update { it.copy(userMessage = "Đã xóa câu hỏi") }
            loadQuestions()
        }
    }

    fun resetDefaultQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = repository.resetDefaultQuestions()
            _uiState.update {
                it.copy(
                    questions = list,
                    filteredQuestions = filterQuestionsList(list, it.searchQuery, it.selectedCategory),
                    isLoading = false,
                    userMessage = "Đã khôi phục các câu hỏi mẫu mặc định!"
                )
            }
        }
    }

    // Quiz Play Logic
    fun startQuiz(count: Int = 0, shuffle: Boolean = true, category: String = "Tất cả") {
        var pool = _uiState.value.questions
        if (category != "Tất cả") {
            pool = pool.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (pool.isEmpty()) pool = _uiState.value.questions

        var selected = if (shuffle) pool.shuffled() else pool
        if (count > 0 && count < selected.size) {
            selected = selected.take(count)
        }

        timerJob?.cancel()
        _uiState.update {
            it.copy(
                quizQuestions = selected,
                currentQuestionIndex = 0,
                userAnswers = emptyMap(),
                flaggedQuestionIndices = emptySet(),
                elapsedSeconds = 0,
                isQuizActive = true,
                lastResult = null
            )
        }

        // Start timer
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    fun selectAnswer(optionIndex: Int) {
        val currIdx = _uiState.value.currentQuestionIndex
        val currentAnswers = _uiState.value.userAnswers.toMutableMap()
        currentAnswers[currIdx] = optionIndex
        _uiState.update { it.copy(userAnswers = currentAnswers) }
    }

    fun goToQuestion(index: Int) {
        val total = _uiState.value.quizQuestions.size
        if (index in 0 until total) {
            _uiState.update { it.copy(currentQuestionIndex = index) }
        }
    }

    fun nextQuestion() {
        val total = _uiState.value.quizQuestions.size
        val next = _uiState.value.currentQuestionIndex + 1
        if (next < total) {
            _uiState.update { it.copy(currentQuestionIndex = next) }
        }
    }

    fun previousQuestion() {
        val prev = _uiState.value.currentQuestionIndex - 1
        if (prev >= 0) {
            _uiState.update { it.copy(currentQuestionIndex = prev) }
        }
    }

    fun toggleFlagCurrentQuestion() {
        val curr = _uiState.value.currentQuestionIndex
        val flags = _uiState.value.flaggedQuestionIndices.toMutableSet()
        if (flags.contains(curr)) {
            flags.remove(curr)
        } else {
            flags.add(curr)
        }
        _uiState.update { it.copy(flaggedQuestionIndices = flags) }
    }

    fun submitQuiz(): QuizResult {
        timerJob?.cancel()
        val questions = _uiState.value.quizQuestions
        val answers = _uiState.value.userAnswers
        var correctCount = 0

        for (i in questions.indices) {
            val chosen = answers[i]
            if (chosen != null && chosen == questions[i].correctOptionIndex) {
                correctCount++
            }
        }

        val result = QuizResult(
            totalQuestions = questions.size,
            correctAnswers = correctCount,
            timeSpentSeconds = _uiState.value.elapsedSeconds,
            userAnswers = answers
        )

        // Save history
        viewModelScope.launch {
            repository.saveQuizHistory(correctCount, questions.size, _uiState.value.elapsedSeconds)
            loadHistory()
        }

        _uiState.update {
            it.copy(
                isQuizActive = false,
                lastResult = result
            )
        }
        return result
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun setEditingQuestion(question: Question?) {
        _uiState.update { it.copy(editingQuestion = question) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
