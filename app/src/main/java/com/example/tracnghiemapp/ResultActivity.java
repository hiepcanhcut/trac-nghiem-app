package com.example.tracnghiemapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.adapter.ReviewAdapter;
import com.example.tracnghiemapp.model.Question;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();
    }

    private void initViews() {
        Toolbar toolbarResult = findViewById(R.id.toolbarResult);
        toolbarResult.setNavigationOnClickListener(v -> finish());

        ImageView ivResultIcon = findViewById(R.id.ivResultIcon);
        TextView tvResultRank = findViewById(R.id.tvResultRank);
        TextView tvResultScore = findViewById(R.id.tvResultScore);
        TextView tvResultPercentage = findViewById(R.id.tvResultPercentage);
        TextView tvResultTime = findViewById(R.id.tvResultTime);
        RecyclerView rvReviewQuestions = findViewById(R.id.rvReviewQuestions);
        MaterialButton btnResultRetake = findViewById(R.id.btnResultRetake);
        MaterialButton btnResultHome = findViewById(R.id.btnResultHome);

        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) getIntent().getSerializableExtra("QUESTIONS");
        int[] userAnswers = getIntent().getIntArrayExtra("USER_ANSWERS");
        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);
        int timeSpent = getIntent().getIntExtra("TIME_SPENT", 0);

        int percentage = total > 0 ? (score * 100) / total : 0;

        tvResultScore.setText(score + "/" + total);
        tvResultPercentage.setText(percentage + "%");

        int min = timeSpent / 60;
        int sec = timeSpent % 60;
        tvResultTime.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));

        if (percentage >= 90) {
            tvResultRank.setText("Xuất sắc! 🎉");
            tvResultRank.setTextColor(ContextCompat.getColor(this, R.color.quiz_green));
            ivResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.quiz_green));
        } else if (percentage >= 70) {
            tvResultRank.setText("Khá tốt! 👏");
            tvResultRank.setTextColor(ContextCompat.getColor(this, R.color.primary));
            ivResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.primary));
        } else if (percentage >= 50) {
            tvResultRank.setText("Đạt yêu cầu 👍");
            tvResultRank.setTextColor(ContextCompat.getColor(this, R.color.quiz_yellow));
            ivResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.quiz_yellow));
        } else {
            tvResultRank.setText("Cần cố gắng thêm! 💪");
            tvResultRank.setTextColor(ContextCompat.getColor(this, R.color.quiz_red));
            ivResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.quiz_red));
        }

        // Setup review recycler view
        rvReviewQuestions.setLayoutManager(new LinearLayoutManager(this));
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, questions, userAnswers);
        rvReviewQuestions.setAdapter(reviewAdapter);

        btnResultHome.setOnClickListener(v -> finish());

        btnResultRetake.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY", "Tất cả");
            intent.putExtra("QUESTION_COUNT", total);
            startActivity(intent);
            finish();
        });
    }
}
