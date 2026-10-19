package com.example.tracnghiemapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tracnghiemapp.database.QuizDbHelper;
import com.example.tracnghiemapp.model.Question;
import com.google.android.material.button.MaterialButton;

public class AddEditQuestionActivity extends AppCompatActivity {

    private QuizDbHelper dbHelper;
    private Question questionToEdit;

    private EditText etQuestionCategory, etQuestionText;
    private EditText etOptionA, etOptionB, etOptionC, etOptionD;
    private RadioGroup rgCorrectOption;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD;
    private EditText etExplanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_question);

        dbHelper = new QuizDbHelper(this);
        questionToEdit = (Question) getIntent().getSerializableExtra("QUESTION_TO_EDIT");

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbarAddEdit);
        toolbar.setNavigationOnClickListener(v -> finish());

        etQuestionCategory = findViewById(R.id.etQuestionCategory);
        etQuestionText = findViewById(R.id.etQuestionText);
        etOptionA = findViewById(R.id.etOptionA);
        etOptionB = findViewById(R.id.etOptionB);
        etOptionC = findViewById(R.id.etOptionC);
        etOptionD = findViewById(R.id.etOptionD);
        rgCorrectOption = findViewById(R.id.rgCorrectOption);
        rbOptionA = findViewById(R.id.rbOptionA);
        rbOptionB = findViewById(R.id.rbOptionB);
        rbOptionC = findViewById(R.id.rbOptionC);
        rbOptionD = findViewById(R.id.rbOptionD);
        etExplanation = findViewById(R.id.etExplanation);
        MaterialButton btnSaveQuestion = findViewById(R.id.btnSaveQuestion);

        if (questionToEdit != null) {
            toolbar.setTitle("Sửa câu hỏi");
            btnSaveQuestion.setText("Cập nhật câu hỏi");

            etQuestionCategory.setText(questionToEdit.getCategory());
            etQuestionText.setText(questionToEdit.getQuestionText());
            etOptionA.setText(questionToEdit.getOptionA());
            etOptionB.setText(questionToEdit.getOptionB());
            etOptionC.setText(questionToEdit.getOptionC());
            etOptionD.setText(questionToEdit.getOptionD());
            etExplanation.setText(questionToEdit.getExplanation());

            switch (questionToEdit.getCorrectOptionIndex()) {
                case 0: rbOptionA.setChecked(true); break;
                case 1: rbOptionB.setChecked(true); break;
                case 2: rbOptionC.setChecked(true); break;
                case 3: rbOptionD.setChecked(true); break;
            }
        } else {
            toolbar.setTitle("Thêm câu hỏi mới");
            btnSaveQuestion.setText("Lưu câu hỏi mới");
        }

        btnSaveQuestion.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        String category = etQuestionCategory.getText().toString().trim();
        String text = etQuestionText.getText().toString().trim();
        String optA = etOptionA.getText().toString().trim();
        String optB = etOptionB.getText().toString().trim();
        String optC = etOptionC.getText().toString().trim();
        String optD = etOptionD.getText().toString().trim();
        String explanation = etExplanation.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung câu hỏi!", Toast.LENGTH_SHORT).show();
            etQuestionText.requestFocus();
            return;
        }

        if (optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ cả 4 phương án A, B, C, D!", Toast.LENGTH_SHORT).show();
            return;
        }

        int correctIndex = 0;
        int checkedId = rgCorrectOption.getCheckedRadioButtonId();
        if (checkedId == R.id.rbOptionB) {
            correctIndex = 1;
        } else if (checkedId == R.id.rbOptionC) {
            correctIndex = 2;
        } else if (checkedId == R.id.rbOptionD) {
            correctIndex = 3;
        }

        if (category.isEmpty()) {
            category = "Tổng hợp";
        }

        if (questionToEdit == null) {
            Question newQ = new Question(text, optA, optB, optC, optD, correctIndex, explanation, category);
            dbHelper.insertQuestion(newQ);
            Toast.makeText(this, "Đã thêm câu hỏi mới thành công!", Toast.LENGTH_SHORT).show();
        } else {
            questionToEdit.setCategory(category);
            questionToEdit.setQuestionText(text);
            questionToEdit.setOptionA(optA);
            questionToEdit.setOptionB(optB);
            questionToEdit.setOptionC(optC);
            questionToEdit.setOptionD(optD);
            questionToEdit.setCorrectOptionIndex(correctIndex);
            questionToEdit.setExplanation(explanation);
            dbHelper.updateQuestion(questionToEdit);
            Toast.makeText(this, "Đã cập nhật câu hỏi thành công!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
