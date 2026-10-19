package com.example.tracnghiemapp.model;

import java.io.Serializable;

public class Question implements Serializable {
    private long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctOptionIndex; // 0: A, 1: B, 2: C, 3: D
    private String explanation;
    private String category;

    public Question() {
        this.category = "Tổng hợp";
        this.explanation = "";
    }

    public Question(String questionText, String optionA, String optionB, String optionC, String optionD,
                    int correctOptionIndex, String explanation, String category) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.category = (category == null || category.trim().isEmpty()) ? "Tổng hợp" : category.trim();
    }

    public Question(long id, String questionText, String optionA, String optionB, String optionC, String optionD,
                    int correctOptionIndex, String explanation, String category) {
        this(questionText, optionA, optionB, optionC, optionD, correctOptionIndex, explanation, category);
        this.id = id;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }

    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }

    public String getExplanation() { return explanation == null ? "" : explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getCategory() { return (category == null || category.trim().isEmpty()) ? "Tổng hợp" : category; }
    public void setCategory(String category) { this.category = category; }

    public String getOption(int index) {
        switch (index) {
            case 0: return optionA;
            case 1: return optionB;
            case 2: return optionC;
            case 3: return optionD;
            default: return "";
        }
    }

    public String getCorrectLetter() {
        switch (correctOptionIndex) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            default: return "A";
        }
    }
}
