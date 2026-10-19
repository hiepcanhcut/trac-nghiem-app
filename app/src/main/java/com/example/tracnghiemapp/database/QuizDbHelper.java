package com.example.tracnghiemapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tracnghiemapp.model.Question;
import com.example.tracnghiemapp.model.QuizHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tracnghiem_java.db";
    public static final int DATABASE_VERSION = 1;

    // Table Questions
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COL_ID = "id";
    public static final String COL_QUESTION_TEXT = "question_text";
    public static final String COL_OPTION_A = "option_a";
    public static final String COL_OPTION_B = "option_b";
    public static final String COL_OPTION_C = "option_c";
    public static final String COL_OPTION_D = "option_d";
    public static final String COL_CORRECT_OPTION = "correct_option";
    public static final String COL_EXPLANATION = "explanation";
    public static final String COL_CATEGORY = "category";

    // Table History
    public static final String TABLE_HISTORY = "quiz_history";
    public static final String COL_HIST_ID = "id";
    public static final String COL_HIST_DATE = "date_millis";
    public static final String COL_HIST_SCORE = "score";
    public static final String COL_HIST_TOTAL = "total_questions";
    public static final String COL_HIST_TIME = "time_spent_seconds";

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_QUESTION_TEXT + " TEXT NOT NULL, "
                + COL_OPTION_A + " TEXT NOT NULL, "
                + COL_OPTION_B + " TEXT NOT NULL, "
                + COL_OPTION_C + " TEXT NOT NULL, "
                + COL_OPTION_D + " TEXT NOT NULL, "
                + COL_CORRECT_OPTION + " INTEGER NOT NULL, "
                + COL_EXPLANATION + " TEXT, "
                + COL_CATEGORY + " TEXT NOT NULL);";

        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " ("
                + COL_HIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_HIST_DATE + " INTEGER NOT NULL, "
                + COL_HIST_SCORE + " INTEGER NOT NULL, "
                + COL_HIST_TOTAL + " INTEGER NOT NULL, "
                + COL_HIST_TIME + " INTEGER NOT NULL);";

        db.execSQL(createQuestionsTable);
        db.execSQL(createHistoryTable);

        seedDefaultQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    private void seedDefaultQuestions(SQLiteDatabase db) {
        List<Question> samples = new ArrayList<>();
        samples.add(new Question(
                "Thủ đô của nước Cộng hòa Xã hội Chủ nghĩa Việt Nam là gì?",
                "Hà Nội", "Thành phố Hồ Chí Minh", "Đà Nẵng", "Huế",
                0, "Hà Nội là thủ đô của Việt Nam từ năm 1976 sau khi đất nước thống nhất.", "Địa lý"
        ));
        samples.add(new Question(
                "Kim loại nào có khả năng dẫn điện tốt nhất ở điều kiện tiêu chuẩn?",
                "Đồng (Cu)", "Vàng (Au)", "Bạc (Ag)", "Nhôm (Al)",
                2, "Bạc là kim loại dẫn điện và dẫn nhiệt tốt nhất trong tất cả các kim loại.", "Khoa học"
        ));
        samples.add(new Question(
                "Ngôn ngữ lập trình chính thống được sử dụng nhiều nhất từ trước đến nay trong phát triển ứng dụng Android là gì?",
                "Java", "Ruby", "PHP", "Swift",
                0, "Java là ngôn ngữ nền tảng kinh điển của hệ điều hành Android từ những ngày đầu.", "Công nghệ"
        ));
        samples.add(new Question(
                "Đỉnh núi nào cao nhất thế giới so với mực nước biển?",
                "K2", "Fansipan", "Everest", "Kangchenjunga",
                2, "Đỉnh Everest nằm trên dãy Himalaya cao 8.848,86 m so với mực nước biển.", "Địa lý"
        ));
        samples.add(new Question(
                "Đơn vị nào dưới đây dùng để đo cường độ dòng điện trong hệ đo lường quốc tế (SI)?",
                "Vôn (V)", "Ampe (A)", "Ôm (Ω)", "Oát (W)",
                1, "Ampe (ký hiệu: A) là đơn vị chuẩn đo cường độ dòng điện trong hệ SI.", "Khoa học"
        ));
        samples.add(new Question(
                "Bức họa nổi tiếng 'Mona Lisa' là kiệt tác của danh họa nào thời Phục Hưng?",
                "Vincent van Gogh", "Pablo Picasso", "Michelangelo", "Leonardo da Vinci",
                3, "Leonardo da Vinci đã vẽ kiệt tác Mona Lisa vào đầu thế kỷ 16.", "Nghệ thuật"
        ));
        samples.add(new Question(
                "Hành tinh nào có kích thước và khối lượng lớn nhất trong Hệ Mặt Trời?",
                "Sao Thổ", "Sao Mộc", "Sao Hải Vương", "Sao Hỏa",
                1, "Sao Mộc (Jupiter) là hành tinh lớn nhất trong Hệ Mặt Trời, lớn hơn tất cả các hành tinh khác gộp lại.", "Thiên văn"
        ));
        samples.add(new Question(
                "Năm nào đánh dấu sự kiện Chủ tịch Hồ Chí Minh đọc bản Tuyên ngôn Độc lập tại Quảng trường Ba Đình?",
                "1930", "1945", "1954", "1975",
                1, "Vào ngày 2 tháng 9 năm 1945, Chủ tịch Hồ Chí Minh đã đọc Tuyên ngôn Độc lập khai sinh nước Việt Nam Dân chủ Cộng hòa.", "Lịch sử"
        ));

        for (Question q : samples) {
            ContentValues values = new ContentValues();
            values.put(COL_QUESTION_TEXT, q.getQuestionText());
            values.put(COL_OPTION_A, q.getOptionA());
            values.put(COL_OPTION_B, q.getOptionB());
            values.put(COL_OPTION_C, q.getOptionC());
            values.put(COL_OPTION_D, q.getOptionD());
            values.put(COL_CORRECT_OPTION, q.getCorrectOptionIndex());
            values.put(COL_EXPLANATION, q.getExplanation());
            values.put(COL_CATEGORY, q.getCategory());
            db.insert(TABLE_QUESTIONS, null, values);
        }
    }

    public long insertQuestion(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QUESTION_TEXT, q.getQuestionText());
        values.put(COL_OPTION_A, q.getOptionA());
        values.put(COL_OPTION_B, q.getOptionB());
        values.put(COL_OPTION_C, q.getOptionC());
        values.put(COL_OPTION_D, q.getOptionD());
        values.put(COL_CORRECT_OPTION, q.getCorrectOptionIndex());
        values.put(COL_EXPLANATION, q.getExplanation());
        values.put(COL_CATEGORY, q.getCategory());
        return db.insert(TABLE_QUESTIONS, null, values);
    }

    public int updateQuestion(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QUESTION_TEXT, q.getQuestionText());
        values.put(COL_OPTION_A, q.getOptionA());
        values.put(COL_OPTION_B, q.getOptionB());
        values.put(COL_OPTION_C, q.getOptionC());
        values.put(COL_OPTION_D, q.getOptionD());
        values.put(COL_CORRECT_OPTION, q.getCorrectOptionIndex());
        values.put(COL_EXPLANATION, q.getExplanation());
        values.put(COL_CATEGORY, q.getCategory());
        return db.update(TABLE_QUESTIONS, values, COL_ID + " = ?", new String[]{String.valueOf(q.getId())});
    }

    public int deleteQuestion(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_QUESTIONS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, null, null, null, null, COL_ID + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Question q = new Question(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_QUESTION_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTION_A)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTION_B)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTION_C)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_OPTION_D)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CORRECT_OPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPLANATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY))
                );
                list.add(q);
            }
            cursor.close();
        }
        return list;
    }

    public List<Question> getQuestions(String query, String category) {
        List<Question> all = getAllQuestions();
        List<Question> filtered = new ArrayList<>();
        for (Question q : all) {
            boolean matchCategory = category == null || category.equals("Tất cả") || q.getCategory().equalsIgnoreCase(category);
            boolean matchQuery = query == null || query.trim().isEmpty()
                    || q.getQuestionText().toLowerCase().contains(query.toLowerCase())
                    || q.getOptionA().toLowerCase().contains(query.toLowerCase())
                    || q.getOptionB().toLowerCase().contains(query.toLowerCase())
                    || q.getOptionC().toLowerCase().contains(query.toLowerCase())
                    || q.getOptionD().toLowerCase().contains(query.toLowerCase());
            if (matchCategory && matchQuery) {
                filtered.add(q);
            }
        }
        return filtered;
    }

    public List<String> getCategories() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COL_CATEGORY + " FROM " + TABLE_QUESTIONS, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String cat = cursor.getString(0);
                if (cat != null && !cat.trim().isEmpty() && !list.contains(cat.trim())) {
                    list.add(cat.trim());
                }
            }
            cursor.close();
        }
        return list;
    }

    public int getQuestionsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUESTIONS, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public void resetDefaultQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, null, null);
        seedDefaultQuestions(db);
    }

    public long insertHistory(QuizHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HIST_DATE, history.getDateMillis());
        values.put(COL_HIST_SCORE, history.getScore());
        values.put(COL_HIST_TOTAL, history.getTotalQuestions());
        values.put(COL_HIST_TIME, history.getTimeSpentSeconds());
        return db.insert(TABLE_HISTORY, null, values);
    }

    public List<QuizHistory> getAllHistory() {
        List<QuizHistory> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null, COL_HIST_ID + " DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuizHistory h = new QuizHistory(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_HIST_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_HIST_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_HIST_SCORE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_HIST_TOTAL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_HIST_TIME))
                );
                list.add(h);
            }
            cursor.close();
        }
        return list;
    }
}
