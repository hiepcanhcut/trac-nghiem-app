package com.example.tracnghiemapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.tracnghiemapp.database.QuizDbHelper;
import com.example.tracnghiemapp.model.Question;
import com.example.tracnghiemapp.model.QuizHistory;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    private QuizDbHelper dbHelper;
    private List<Question> questionList = new ArrayList<>();
    private int[] userAnswers;
    private final Set<Integer> flaggedQuestions = new HashSet<>();

    private int currentIndex = 0;
    private int elapsedSeconds = 0;
    private Handler timerHandler;
    private Runnable timerRunnable;

    private TextView tvQuizQuestionProgress, tvQuizTimer, tvQuizCategory, tvQuizQuestionText;
    private ProgressBar progressBarQuiz;
    private ImageView ivFlagQuestion;
    private LinearLayout layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private MaterialButton btnQuizPrev, btnQuizNextOrSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = new QuizDbHelper(this);

        initViews();
        loadQuestions();
        startTimer();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmDialog();
            }
        });
    }

    private void initViews() {
        Toolbar toolbarQuiz = findViewById(R.id.toolbarQuiz);
        toolbarQuiz.setNavigationOnClickListener(v -> showExitConfirmDialog());

        tvQuizQuestionProgress = findViewById(R.id.tvQuizQuestionProgress);
        tvQuizTimer = findViewById(R.id.tvQuizTimer);
        tvQuizCategory = findViewById(R.id.tvQuizCategory);
        tvQuizQuestionText = findViewById(R.id.tvQuizQuestionText);
        progressBarQuiz = findViewById(R.id.progressBarQuiz);
        ivFlagQuestion = findViewById(R.id.ivFlagQuestion);
        ImageView ivQuestionGrid = findViewById(R.id.ivQuestionGrid);

        layoutOptionA = findViewById(R.id.layoutOptionA);
        layoutOptionB = findViewById(R.id.layoutOptionB);
        layoutOptionC = findViewById(R.id.layoutOptionC);
        layoutOptionD = findViewById(R.id.layoutOptionD);

        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);

        btnQuizPrev = findViewById(R.id.btnQuizPrev);
        btnQuizNextOrSubmit = findViewById(R.id.btnQuizNextOrSubmit);

        layoutOptionA.setOnClickListener(v -> selectOption(0));
        layoutOptionB.setOnClickListener(v -> selectOption(1));
        layoutOptionC.setOnClickListener(v -> selectOption(2));
        layoutOptionD.setOnClickListener(v -> selectOption(3));

        btnQuizPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        btnQuizNextOrSubmit.setOnClickListener(v -> {
            if (currentIndex == questionList.size() - 1) {
                showSubmitConfirmDialog();
            } else {
                currentIndex++;
                displayQuestion();
            }
        });

        ivFlagQuestion.setOnClickListener(v -> {
            if (flaggedQuestions.contains(currentIndex)) {
                flaggedQuestions.remove(currentIndex);
            } else {
                flaggedQuestions.add(currentIndex);
            }
            updateFlagIcon();
        });

        ivQuestionGrid.setOnClickListener(v -> showQuestionGridDialog());
    }

    private void loadQuestions() {
        String category = getIntent().getStringExtra("CATEGORY");
        int count = getIntent().getIntExtra("QUESTION_COUNT", 10);

        List<Question> pool = dbHelper.getQuestions("", category);
        if (pool.isEmpty()) {
            pool = dbHelper.getAllQuestions();
        }

        Collections.shuffle(pool);

        if (count > 0 && count < pool.size()) {
            questionList = new ArrayList<>(pool.subList(0, count));
        } else {
            questionList = new ArrayList<>(pool);
        }

        userAnswers = new int[questionList.size()];
        Arrays.fill(userAnswers, -1);

        displayQuestion();
    }

    private void displayQuestion() {
        if (questionList.isEmpty()) return;

        Question q = questionList.get(currentIndex);
        int total = questionList.size();

        tvQuizQuestionProgress.setText("Câu " + (currentIndex + 1) + "/" + total);
        progressBarQuiz.setProgress(((currentIndex + 1) * 100) / total);

        tvQuizCategory.setText(q.getCategory());
        tvQuizQuestionText.setText(q.getQuestionText());

        tvOptionA.setText(q.getOptionA());
        tvOptionB.setText(q.getOptionB());
        tvOptionC.setText(q.getOptionC());
        tvOptionD.setText(q.getOptionD());

        updateOptionStyles();
        updateFlagIcon();

        btnQuizPrev.setEnabled(currentIndex > 0);

        if (currentIndex == total - 1) {
            btnQuizNextOrSubmit.setText("Nộp bài");
            btnQuizNextOrSubmit.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.quiz_green));
        } else {
            btnQuizNextOrSubmit.setText("Câu sau");
            btnQuizNextOrSubmit.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
        }
    }

    private void selectOption(int optionIndex) {
        userAnswers[currentIndex] = optionIndex;
        updateOptionStyles();
    }

    private void updateOptionStyles() {
        int selected = userAnswers[currentIndex];

        layoutOptionA.setBackgroundResource(selected == 0 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        layoutOptionB.setBackgroundResource(selected == 1 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        layoutOptionC.setBackgroundResource(selected == 2 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
        layoutOptionD.setBackgroundResource(selected == 3 ? R.drawable.bg_option_selected : R.drawable.bg_option_default);
    }

    private void updateFlagIcon() {
        if (flaggedQuestions.contains(currentIndex)) {
            ivFlagQuestion.setImageResource(android.R.drawable.btn_star_big_on);
            ivFlagQuestion.setColorFilter(ContextCompat.getColor(this, R.color.quiz_yellow));
        } else {
            ivFlagQuestion.setImageResource(android.R.drawable.btn_star_big_off);
            ivFlagQuestion.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }

    private void startTimer() {
        timerHandler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                elapsedSeconds++;
                int min = elapsedSeconds / 60;
                int sec = elapsedSeconds % 60;
                tvQuizTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void showQuestionGridDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_question_grid, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView tvGridTitle = view.findViewById(R.id.tvGridTitle);
        int answeredCount = getAnsweredCount();
        tvGridTitle.setText("Bảng câu hỏi (" + answeredCount + "/" + questionList.size() + " đã làm)");

        GridView gridView = view.findViewById(R.id.gridViewQuestions);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return questionList.size(); }
            @Override
            public Object getItem(int position) { return position; }
            @Override
            public long getItemId(int position) { return position; }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_number, parent, false);
                }
                TextView tvNum = convertView.findViewById(R.id.tvGridNumber);
                tvNum.setText(String.valueOf(position + 1));

                boolean isAnswered = userAnswers[position] != -1;
                boolean isFlag = flaggedQuestions.contains(position);
                boolean isCurrent = (position == currentIndex);

                if (isCurrent) {
                    tvNum.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.primary));
                    tvNum.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));
                } else if (isFlag) {
                    tvNum.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.quiz_yellow));
                    tvNum.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));
                } else if (isAnswered) {
                    tvNum.setBackgroundResource(R.drawable.bg_option_selected);
                    tvNum.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.primary));
                } else {
                    tvNum.setBackgroundResource(R.drawable.bg_option_default);
                    tvNum.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.text_secondary));
                }

                convertView.setOnClickListener(v -> {
                    currentIndex = position;
                    displayQuestion();
                    dialog.dismiss();
                });

                return convertView;
            }
        });

        MaterialButton btnSubmitFromGrid = view.findViewById(R.id.btnSubmitFromGrid);
        btnSubmitFromGrid.setOnClickListener(v -> {
            dialog.dismiss();
            showSubmitConfirmDialog();
        });

        dialog.show();
    }

    private int getAnsweredCount() {
        int count = 0;
        for (int a : userAnswers) {
            if (a != -1) count++;
        }
        return count;
    }

    private void showSubmitConfirmDialog() {
        int total = questionList.size();
        int answered = getAnsweredCount();
        int unanswered = total - answered;

        String msg = "Bạn đã hoàn thành: " + answered + "/" + total + " câu.";
        if (unanswered > 0) {
            msg += "\n\n⚠️ Lưu ý: Còn " + unanswered + " câu chưa chọn đáp án!";
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận nộp bài?")
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Nộp bài", (dialog, which) -> submitQuiz())
                .setNegativeButton("Làm tiếp", null)
                .show();
    }

    private void submitQuiz() {
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        int correctCount = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers[i] == questionList.get(i).getCorrectOptionIndex()) {
                correctCount++;
            }
        }

        // Save history
        QuizHistory history = new QuizHistory(System.currentTimeMillis(), correctCount, questionList.size(), elapsedSeconds);
        dbHelper.insertHistory(history);

        // Open ResultActivity
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("QUESTIONS", (Serializable) questionList);
        intent.putExtra("USER_ANSWERS", userAnswers);
        intent.putExtra("SCORE", correctCount);
        intent.putExtra("TOTAL", questionList.size());
        intent.putExtra("TIME_SPENT", elapsedSeconds);
        startActivity(intent);
        finish();
    }

    private void showExitConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát bài thi?")
                .setMessage("Nếu bạn thoát ngay bây giờ, toàn bộ bài làm hiện tại sẽ bị hủy.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Thoát", (dialog, which) -> {
                    if (timerHandler != null && timerRunnable != null) {
                        timerHandler.removeCallbacks(timerRunnable);
                    }
                    finish();
                })
                .setNegativeButton("Làm tiếp", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }
}
