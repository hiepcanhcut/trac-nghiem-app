package com.example.tracnghiemapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracnghiemapp.ui.screens.*
import com.example.tracnghiemapp.ui.theme.TracNghiemAppTheme
import com.example.tracnghiemapp.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TracNghiemAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizAppRoot()
                }
            }
        }
    }
}

@Composable
fun QuizAppRoot(
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Show toast for messages
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                uiState = uiState,
                categories = viewModel.getCategories(),
                onStartQuiz = { count, category ->
                    viewModel.startQuiz(count = count, shuffle = true, category = category)
                    currentScreen = Screen.QuizPlay
                },
                onManageQuestions = {
                    currentScreen = Screen.ManageQuestions
                },
                onAddQuestion = {
                    currentScreen = Screen.AddEditQuestion(null)
                },
                onViewHistory = {
                    viewModel.loadHistory()
                    currentScreen = Screen.History
                },
                onResetDefaults = {
                    viewModel.resetDefaultQuestions()
                }
            )
        }

        is Screen.ManageQuestions -> {
            ManageQuestionsScreen(
                uiState = uiState,
                categories = viewModel.getCategories(),
                onSearchChange = { viewModel.onSearchQueryChanged(it) },
                onCategoryChange = { viewModel.onCategoryFilterChanged(it) },
                onAddQuestion = {
                    currentScreen = Screen.AddEditQuestion(null)
                },
                onEditQuestion = { question ->
                    currentScreen = Screen.AddEditQuestion(question)
                },
                onDeleteQuestion = { id ->
                    viewModel.deleteQuestion(id)
                },
                onBack = {
                    currentScreen = Screen.Home
                }
            )
        }

        is Screen.AddEditQuestion -> {
            AddEditQuestionScreen(
                questionToEdit = screen.question,
                existingCategories = viewModel.getCategories(),
                onSave = { id, text, a, b, c, d, correctIdx, explanation, category ->
                    viewModel.saveQuestion(
                        id = id,
                        questionText = text,
                        optionA = a,
                        optionB = b,
                        optionC = c,
                        optionD = d,
                        correctIndex = correctIdx,
                        explanation = explanation,
                        category = category,
                        onSuccess = {
                            currentScreen = Screen.ManageQuestions
                        }
                    )
                },
                onBack = {
                    currentScreen = Screen.ManageQuestions
                }
            )
        }

        is Screen.QuizPlay -> {
            QuizPlayScreen(
                uiState = uiState,
                onSelectAnswer = { viewModel.selectAnswer(it) },
                onGoToQuestion = { viewModel.goToQuestion(it) },
                onNextQuestion = { viewModel.nextQuestion() },
                onPreviousQuestion = { viewModel.previousQuestion() },
                onToggleFlag = { viewModel.toggleFlagCurrentQuestion() },
                onSubmitQuiz = {
                    val result = viewModel.submitQuiz()
                    val questions = uiState.quizQuestions
                    currentScreen = Screen.QuizResultReview(result, questions)
                    result
                },
                onExitQuiz = {
                    currentScreen = Screen.Home
                }
            )
        }

        is Screen.QuizResultReview -> {
            QuizResultScreen(
                result = screen.result,
                questions = screen.questions,
                onRetakeQuiz = {
                    viewModel.startQuiz(
                        count = screen.questions.size,
                        shuffle = true,
                        category = "Tất cả"
                    )
                    currentScreen = Screen.QuizPlay
                },
                onHome = {
                    currentScreen = Screen.Home
                }
            )
        }

        is Screen.History -> {
            HistoryScreen(
                historyList = uiState.historyList,
                onBack = {
                    currentScreen = Screen.Home
                }
            )
        }
    }
}