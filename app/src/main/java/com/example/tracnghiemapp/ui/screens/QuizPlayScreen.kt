package com.example.tracnghiemapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.data.model.QuizResult
import com.example.tracnghiemapp.ui.theme.*
import com.example.tracnghiemapp.ui.viewmodel.QuizUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    uiState: QuizUiState,
    onSelectAnswer: (Int) -> Unit,
    onGoToQuestion: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onToggleFlag: () -> Unit,
    onSubmitQuiz: () -> QuizResult,
    onExitQuiz: () -> Unit
) {
    var showSubmitConfirm by remember { mutableStateOf(false) }
    var showExitConfirm by remember { mutableStateOf(false) }
    var showQuestionGridSheet by remember { mutableStateOf(false) }

    // Intercept back button
    BackHandler {
        showExitConfirm = true
    }

    val questions = uiState.quizQuestions
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có câu hỏi nào để hiển thị")
        }
        return
    }

    val currentIndex = uiState.currentQuestionIndex
    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }
    val totalCount = questions.size
    val answeredCount = uiState.userAnswers.size
    val currentSelectedOption = uiState.userAnswers[currentIndex]
    val isFlagged = uiState.flaggedQuestionIndices.contains(currentIndex)

    // Format timer
    val minutes = uiState.elapsedSeconds / 60
    val seconds = uiState.elapsedSeconds % 60
    val timerString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Câu ${currentIndex + 1}/$totalCount",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        // Timer badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = IndigoPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = timerString,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showExitConfirm = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Thoát")
                    }
                },
                actions = {
                    // Flag button
                    IconButton(onClick = onToggleFlag) {
                        Icon(
                            imageVector = if (isFlagged) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Đánh dấu xem lại",
                            tint = if (isFlagged) AmberAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Grid button
                    IconButton(onClick = { showQuestionGridSheet = true }) {
                        Icon(Icons.Default.GridView, contentDescription = "Danh sách câu")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onPreviousQuestion,
                        enabled = currentIndex > 0,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Câu trước")
                    }

                    if (currentIndex == totalCount - 1) {
                        Button(
                            onClick = { showSubmitConfirm = true },
                            colors = ButtonDefaults.buttonColors(containerColor = QuizGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Nộp bài", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        Button(
                            onClick = onNextQuestion,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("Câu sau")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Linear Progress Bar
            val progress = (currentIndex + 1).toFloat() / totalCount.toFloat()
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = IndigoPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Category tag
            Surface(
                color = IndigoPrimary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = currentQuestion.category,
                    color = IndigoPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = currentQuestion.questionText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 26.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = "Chọn một đáp án đúng:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 4 Options
            val options = currentQuestion.getOptions()
            val optionLetters = listOf("A", "B", "C", "D")

            options.forEachIndexed { index, optionText ->
                val isSelected = currentSelectedOption == index
                QuizOptionCard(
                    letter = optionLetters[index],
                    text = optionText,
                    isSelected = isSelected,
                    onSelect = { onSelectAnswer(index) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Question Grid Bottom Sheet
    if (showQuestionGridSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuestionGridSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Bảng câu hỏi (${answeredCount}/$totalCount đã làm)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LegendItem(color = IndigoPrimary, text = "Đã làm")
                    LegendItem(color = AmberAccent, text = "Đánh dấu")
                    LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, text = "Chưa làm")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 52.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    itemsIndexed(questions) { idx, _ ->
                        val isAnswered = uiState.userAnswers.containsKey(idx)
                        val isFlag = uiState.flaggedQuestionIndices.contains(idx)
                        val isCurrent = idx == currentIndex

                        val bgColor = when {
                            isCurrent -> IndigoPrimary
                            isFlag -> AmberAccent
                            isAnswered -> IndigoPrimary.copy(alpha = 0.25f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        val textColor = when {
                            isCurrent -> Color.White
                            isFlag -> Color.White
                            isAnswered -> IndigoPrimary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .clickable {
                                    onGoToQuestion(idx)
                                    showQuestionGridSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showQuestionGridSheet = false
                        showSubmitConfirm = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = QuizGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Nộp bài ngay", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirm) {
        val unanswered = totalCount - answeredCount
        AlertDialog(
            onDismissRequest = { showSubmitConfirm = false },
            icon = { Icon(Icons.Default.HelpOutline, contentDescription = null, tint = IndigoPrimary) },
            title = { Text("Xác nhận nộp bài?") },
            text = {
                Column {
                    Text("Bạn đã trả lời: $answeredCount / $totalCount câu.")
                    if (unanswered > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Lưu ý: Còn $unanswered câu chưa chọn đáp án!",
                            color = QuizRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirm = false
                        onSubmitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizGreen)
                ) {
                    Text("Nộp bài")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirm = false }) {
                    Text("Làm tiếp")
                }
            }
        )
    }

    // Exit Confirmation Dialog
    if (showExitConfirm) {
        AlertDialog(
            onDismissRequest = { showExitConfirm = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = QuizRed) },
            title = { Text("Thoát bài thi?") },
            text = { Text("Nếu thoát bây giờ, quá trình làm bài và câu trả lời của bạn sẽ không được lưu.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirm = false
                        onExitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizRed)
                ) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirm = false }) {
                    Text("Tiếp tục thi")
                }
            }
        )
    }
}

@Composable
fun QuizOptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.outlineVariant,
        label = "borderColor"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) IndigoPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        label = "bgColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(borderColor)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                lineHeight = 22.sp
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = IndigoPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
