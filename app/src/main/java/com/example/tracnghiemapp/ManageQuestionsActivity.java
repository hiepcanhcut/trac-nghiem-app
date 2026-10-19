package com.example.tracnghiemapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.adapter.QuestionAdapter;
import com.example.tracnghiemapp.database.QuizDbHelper;
import com.example.tracnghiemapp.model.Question;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ManageQuestionsActivity extends AppCompatActivity {

    private QuizDbHelper dbHelper;
    private QuestionAdapter adapter;
    private EditText etSearchQuestion;
    private Spinner spinnerFilterCategory;
    private LinearLayout layoutEmptyState;
    private RecyclerView rvManageQuestions;

    private String currentCategory = "Tất cả";
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);

        dbHelper = new QuizDbHelper(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
        filterQuestions();
    }

    private void initViews() {
        Toolbar toolbarManage = findViewById(R.id.toolbarManage);
        toolbarManage.setNavigationOnClickListener(v -> finish());

        etSearchQuestion = findViewById(R.id.etSearchQuestion);
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        rvManageQuestions = findViewById(R.id.rvManageQuestions);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddQuestionFromManage);

        rvManageQuestions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionAdapter(this, new QuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onEdit(Question question) {
                Intent intent = new Intent(ManageQuestionsActivity.this, AddEditQuestionActivity.class);
                intent.putExtra("QUESTION_TO_EDIT", question);
                startActivity(intent);
            }

            @Override
            public void onDelete(Question question) {
                showDeleteConfirmDialog(question);
            }
        });
        rvManageQuestions.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ManageQuestionsActivity.this, AddEditQuestionActivity.class);
            startActivity(intent);
        });

        etSearchQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                filterQuestions();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = parent.getItemAtPosition(position).toString();
                filterQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadCategories() {
        List<String> categories = dbHelper.getCategories();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerFilterCategory.setAdapter(spinnerAdapter);
    }

    private void filterQuestions() {
        List<Question> list = dbHelper.getQuestions(currentQuery, currentCategory);
        adapter.setQuestions(list);

        if (list.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvManageQuestions.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvManageQuestions.setVisibility(View.VISIBLE);
        }
    }

    private void showDeleteConfirmDialog(Question question) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa câu hỏi?")
                .setMessage("Bạn có chắc chắn muốn xóa câu hỏi: \"" + question.getQuestionText() + "\"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteQuestion(question.getId());
                    filterQuestions();
                    loadCategories();
                    Toast.makeText(ManageQuestionsActivity.this, "Đã xóa câu hỏi", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
