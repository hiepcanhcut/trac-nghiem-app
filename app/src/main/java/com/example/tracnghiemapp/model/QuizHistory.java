package com.example.tracnghiemapp.model;

import java.io.Serializable;

public class QuizHistory implements Serializable {
    private long id;
    private long dateMillis;
    private int score;
    private int totalQuestions;
    private int timeSpentSeconds;

    public QuizHistory() {}

    public QuizHistory(long dateMillis, int score, int totalQuestions, int timeSpentSeconds) {
        this.dateMillis = dateMillis;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public QuizHistory(long id, long dateMillis, int score, int totalQuestions, int timeSpentSeconds) {
        this.id = id;
        this.dateMillis = dateMillis;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getDateMillis() { return dateMillis; }
    public void setDateMillis(long dateMillis) { this.dateMillis = dateMillis; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(int timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }

    public int getPercentage() {
        return totalQuestions > 0 ? (score * 100) / totalQuestions : 0;
    }
}
