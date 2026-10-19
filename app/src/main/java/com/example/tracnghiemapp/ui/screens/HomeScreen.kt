package com.example.tracnghiemapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracnghiemapp.ui.theme.*
import com.example.tracnghiemapp.ui.viewmodel.QuizUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: QuizUiState,
    categories: List<String>,
    onStartQuiz: (count: Int, category: String) -> Unit,
    onManageQuestions: () -> Unit,
    onAddQuestion: () -> Unit,
    onViewHistory: () -> Unit,
    onResetDefaults: () -> Unit
) {
    var showStartDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Tất cả") }
    var selectedQuestionCount by remember { mutableIntStateOf(10) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Trắc Nghiệm Pro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showResetConfirm = true }) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Khôi phục câu hỏi mẫu",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddQuestion,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Thêm câu hỏi") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
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
            // Hero Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(IndigoPrimary, IndigoSecondary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Ôn Luyện & Kiểm Tra",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Luyện Thi Trắc Nghiệm Mọi Lúc",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tự do thêm ngân hàng câu hỏi, kiểm tra kiến thức và xem giải thích chi tiết đáp án.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (uiState.questions.isNotEmpty()) {
                                    showStartDialog = true
                                }
                            },
                            enabled = uiState.questions.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = IndigoPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.questions.isEmpty()) "Chưa có câu hỏi" else "Bắt đầu làm bài",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Tổng câu hỏi",
                    value = "${uiState.questions.size}",
                    icon = Icons.Default.HelpOutline,
                    color = QuizBlue,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Bài đã thi",
                    value = "${uiState.historyList.size}",
                    icon = Icons.Default.AssignmentTurnedIn,
                    color = QuizGreen,
                    modifier = Modifier.weight(1f)
                )
                val avgScore = if (uiState.historyList.isNotEmpty()) {
                    val sum = uiState.historyList.sumOf { it.percentage }
                    "${sum / uiState.historyList.size}%"
                } else "---"
                StatCard(
                    title = "Điểm TB",
                    value = avgScore,
                    icon = Icons.Default.Grade,
                    color = QuizYellow,
                    modifier = Modifier.weight(1f)
                )
            }

            // Section: Main Actions
            Text(
                text = "Chức năng",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            ActionMenuCard(
                title = "Quản lý ngân hàng câu hỏi",
                description = "Xem, tìm kiếm, sửa và xóa câu hỏi có sẵn",
                icon = Icons.Default.LibraryBooks,
                iconColor = IndigoPrimary,
                onClick = onManageQuestions
            )

            ActionMenuCard(
                title = "Thêm câu hỏi mới",
                description = "Tạo câu hỏi trắc nghiệm gồm 4 đáp án A, B, C, D",
                icon = Icons.Default.PostAdd,
                iconColor = QuizGreen,
                onClick = onAddQuestion
            )

            ActionMenuCard(
                title = "Lịch sử & Kết quả thi",
                description = "Xem lại điểm số và quá trình rèn luyện",
                icon = Icons.Default.History,
                iconColor = QuizYellow,
                onClick = onViewHistory
            )

            Spacer(modifier = Modifier.height(60.dp)) // padding for FAB
        }
    }

    // Start Quiz Dialog
    if (showStartDialog) {
        AlertDialog(
            onDismissRequest = { showStartDialog = false },
            title = {
                Text("Tùy chọn bài thi", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Chọn môn học / chủ đề:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    var categoryExpanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(
                            onClick = { categoryExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedCategory)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        selectedCategory = cat
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Số lượng câu hỏi:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 20, 0).forEach { count ->
                            val label = if (count == 0) "Tất cả" else "$count câu"
                            FilterChip(
                                selected = selectedQuestionCount == count,
                                onClick = { selectedQuestionCount = count },
                                label = { Text(label, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showStartDialog = false
                        onStartQuiz(selectedQuestionCount, selectedCategory)
                    }
                ) {
                    Text("Bắt đầu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Reset Defaults Confirmation Dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = QuizYellow) },
            title = { Text("Khôi phục câu hỏi mẫu?") },
            text = {
                Text("Hành động này sẽ thay thế danh sách hiện tại bằng bộ câu hỏi mẫu ban đầu của ứng dụng.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetConfirm = false
                        onResetDefaults()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizRed)
                ) {
                    Text("Khôi phục")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
