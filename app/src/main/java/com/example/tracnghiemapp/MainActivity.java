package com.example.tracnghiemapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracnghiemapp.database.QuizDbHelper;
import com.example.tracnghiemapp.model.QuizHistory;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private QuizDbHelper dbHelper;
    private TextView tvTotalQuestionsCount, tvTotalTestsCount, tvAvgScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new QuizDbHelper(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardStats();
    }

    private void initViews() {
        tvTotalQuestionsCount = findViewById(R.id.tvTotalQuestionsCount);
        tvTotalTestsCount = findViewById(R.id.tvTotalTestsCount);
        tvAvgScore = findViewById(R.id.tvAvgScore);

        MaterialButton btnStartQuizHero = findViewById(R.id.btnStartQuizHero);
        View cardManageQuestions = findViewById(R.id.cardManageQuestions);
        View cardAddQuestion = findViewById(R.id.cardAddQuestion);
        View cardHistory = findViewById(R.id.cardHistory);
        View fabAddQuestion = findViewById(R.id.fabAddQuestion);
        MaterialButton btnResetDefaults = findViewById(R.id.btnResetDefaults);

        btnStartQuizHero.setOnClickListener(v -> showStartQuizDialog());

        cardManageQuestions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManageQuestionsActivity.class);
            startActivity(intent);
        });

        View.OnClickListener openAddQuestion = v -> {
            Intent intent = new Intent(MainActivity.this, AddEditQuestionActivity.class);
            startActivity(intent);
        };
        cardAddQuestion.setOnClickListener(openAddQuestion);
        fabAddQuestion.setOnClickListener(openAddQuestion);

        cardHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnResetDefaults.setOnClickListener(v -> showResetConfirmDialog());
    }

    private void loadDashboardStats() {
        int totalQuestions = dbHelper.getQuestionsCount();
        tvTotalQuestionsCount.setText(String.valueOf(totalQuestions));

        List<QuizHistory> histories = dbHelper.getAllHistory();
        tvTotalTestsCount.setText(String.valueOf(histories.size()));

        if (!histories.isEmpty()) {
            int sum = 0;
            for (QuizHistory h : histories) {
                sum += h.getPercentage();
            }
            int avg = sum / histories.size();
            tvAvgScore.setText(avg + "%");
        } else {
            tvAvgScore.setText("---");
        }
    }

    private void showStartQuizDialog() {
        int totalQuestions = dbHelper.getQuestionsCount();
        if (totalQuestions == 0) {
            Toast.makeText(this, "Ngân hàng câu hỏi đang trống! Hãy thêm câu hỏi trước.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quiz_options, null);
        builder.setView(dialogView);

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        RadioGroup radioGroupCount = dialogView.findViewById(R.id.radioGroupCount);
        RadioButton rbCount5 = dialogView.findViewById(R.id.rbCount5);
        RadioButton rbCount10 = dialogView.findViewById(R.id.rbCount10);
        RadioButton rbCount20 = dialogView.findViewById(R.id.rbCount20);

        List<String> categories = dbHelper.getCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        builder.setPositiveButton("Bắt đầu", (dialog, which) -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();
            int questionCount = 10;
            int checkedId = radioGroupCount.getCheckedRadioButtonId();
            if (checkedId == R.id.rbCount5) {
                questionCount = 5;
            } else if (checkedId == R.id.rbCount10) {
                questionCount = 10;
            } else if (checkedId == R.id.rbCount20) {
                questionCount = 20;
            } else if (checkedId == R.id.rbCountAll) {
                questionCount = 0; // 0 means all
            }

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY", selectedCategory);
            intent.putExtra("QUESTION_COUNT", questionCount);
            startActivity(intent);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showResetConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Khôi phục câu hỏi mẫu?")
                .setMessage("Thao tác này sẽ đặt lại ngân hàng câu hỏi về bộ câu hỏi mẫu ban đầu của ứng dụng.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Khôi phục", (dialog, which) -> {
                    dbHelper.resetDefaultQuestions();
                    loadDashboardStats();
                    Toast.makeText(MainActivity.this, "Đã khôi phục câu hỏi mẫu thành công!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
