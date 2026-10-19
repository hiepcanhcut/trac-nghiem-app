package com.example.tracnghiemapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.ui.theme.IndigoPrimary
import com.example.tracnghiemapp.ui.theme.QuizGreen
import com.example.tracnghiemapp.ui.theme.QuizRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditQuestionScreen(
    questionToEdit: Question? = null,
    existingCategories: List<String>,
    onSave: (
        id: Long,
        questionText: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctIndex: Int,
        explanation: String,
        category: String
    ) -> Unit,
    onBack: () -> Unit
) {
    var questionText by remember { mutableStateOf(questionToEdit?.questionText ?: "") }
    var category by remember { mutableStateOf(questionToEdit?.category ?: "Tổng hợp") }
    var optionA by remember { mutableStateOf(questionToEdit?.optionA ?: "") }
    var optionB by remember { mutableStateOf(questionToEdit?.optionB ?: "") }
    var optionC by remember { mutableStateOf(questionToEdit?.optionC ?: "") }
    var optionD by remember { mutableStateOf(questionToEdit?.optionD ?: "") }
    var correctIndex by remember { mutableIntStateOf(questionToEdit?.correctOptionIndex ?: 0) }
    var explanation by remember { mutableStateOf(questionToEdit?.explanation ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isEditing = questionToEdit != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Sửa câu hỏi" else "Thêm câu hỏi mới",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (questionText.isBlank()) {
                                errorMessage = "Vui lòng nhập nội dung câu hỏi!"
                                return@IconButton
                            }
                            if (optionA.isBlank() || optionB.isBlank() || optionC.isBlank() || optionD.isBlank()) {
                                errorMessage = "Vui lòng nhập đủ cả 4 phương án A, B, C, D!"
                                return@IconButton
                            }

                            errorMessage = null
                            onSave(
                                questionToEdit?.id ?: 0L,
                                questionText,
                                optionA,
                                optionB,
                                optionC,
                                optionD,
                                correctIndex,
                                explanation,
                                category
                            )
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Lưu", tint = IndigoPrimary)
                    }
                }
            )
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
            // Error banner
            if (errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = QuizRed.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = QuizRed,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Category section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Chủ đề / Môn học",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    placeholder = { Text("Ví dụ: Toán, Lịch sử, Tin học, Tiếng Anh...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Quick suggestions
                val suggestions = listOf("Tổng hợp", "Lịch sử", "Địa lý", "Khoa học", "Công nghệ", "Toán học")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    suggestions.take(4).forEach { sug ->
                        SuggestionChip(
                            onClick = { category = sug },
                            label = { Text(sug, fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Question Text Input
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Nội dung câu hỏi *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    placeholder = { Text("Nhập nội dung câu hỏi tại đây...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5
                )
            }

            // Options Input & Correct Answer Selector
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Các phương án trả lời (Nhấn nút tròn để chọn đáp án đúng) *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OptionInputField(
                    letter = "A",
                    value = optionA,
                    onValueChange = { optionA = it },
                    isCorrect = correctIndex == 0,
                    onSelectAsCorrect = { correctIndex = 0 }
                )

                OptionInputField(
                    letter = "B",
                    value = optionB,
                    onValueChange = { optionB = it },
                    isCorrect = correctIndex == 1,
                    onSelectAsCorrect = { correctIndex = 1 }
                )

                OptionInputField(
                    letter = "C",
                    value = optionC,
                    onValueChange = { optionC = it },
                    isCorrect = correctIndex == 2,
                    onSelectAsCorrect = { correctIndex = 2 }
                )

                OptionInputField(
                    letter = "D",
                    value = optionD,
                    onValueChange = { optionD = it },
                    isCorrect = correctIndex == 3,
                    onSelectAsCorrect = { correctIndex = 3 }
                )
            }

            // Explanation section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Giải thích đáp án (Tùy chọn)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    placeholder = { Text("Giải thích lý do vì sao đáp án này đúng để người học nắm rõ kiến thức...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Save Button
            Button(
                onClick = {
                    if (questionText.isBlank()) {
                        errorMessage = "Vui lòng nhập nội dung câu hỏi!"
                        return@Button
                    }
                    if (optionA.isBlank() || optionB.isBlank() || optionC.isBlank() || optionD.isBlank()) {
                        errorMessage = "Vui lòng nhập đủ cả 4 phương án A, B, C, D!"
                        return@Button
                    }

                    errorMessage = null
                    onSave(
                        questionToEdit?.id ?: 0L,
                        questionText,
                        optionA,
                        optionB,
                        optionC,
                        optionD,
                        correctIndex,
                        explanation,
                        category
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Cập nhật câu hỏi" else "Lưu câu hỏi mới",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OptionInputField(
    letter: String,
    value: String,
    onValueChange: (String) -> Unit,
    isCorrect: Boolean,
    onSelectAsCorrect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) QuizGreen.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isCorrect) QuizGreen else MaterialTheme.colorScheme.outlineVariant
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio-like Selector button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isCorrect) QuizGreen else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onSelectAsCorrect() },
                contentAlignment = Alignment.Center
            ) {
                if (isCorrect) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Đáp án đúng",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = letter,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Phương án $letter") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = if (isCorrect) QuizGreen else IndigoPrimary
                )
            )
        }
    }
}
