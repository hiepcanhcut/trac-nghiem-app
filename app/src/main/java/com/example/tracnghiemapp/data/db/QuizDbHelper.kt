package com.example.tracnghiemapp.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tracnghiemapp.data.model.Question
import com.example.tracnghiemapp.data.model.QuizHistory

class QuizDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "tracnghiem.db"
        const val DATABASE_VERSION = 1

        // Questions table
        const val TABLE_QUESTIONS = "questions"
        const val COL_ID = "id"
        const val COL_QUESTION_TEXT = "question_text"
        const val COL_OPTION_A = "option_a"
        const val COL_OPTION_B = "option_b"
        const val COL_OPTION_C = "option_c"
        const val COL_OPTION_D = "option_d"
        const val COL_CORRECT_OPTION = "correct_option"
        const val COL_EXPLANATION = "explanation"
        const val COL_CATEGORY = "category"

        // History table
        const val TABLE_HISTORY = "quiz_history"
        const val COL_HIST_ID = "id"
        const val COL_HIST_DATE = "date_millis"
        const val COL_HIST_SCORE = "score"
        const val COL_HIST_TOTAL = "total_questions"
        const val COL_HIST_TIME = "time_spent_seconds"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createQuestionsTable = """
            CREATE TABLE $TABLE_QUESTIONS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_QUESTION_TEXT TEXT NOT NULL,
                $COL_OPTION_A TEXT NOT NULL,
                $COL_OPTION_B TEXT NOT NULL,
                $COL_OPTION_C TEXT NOT NULL,
                $COL_OPTION_D TEXT NOT NULL,
                $COL_CORRECT_OPTION INTEGER NOT NULL,
                $COL_EXPLANATION TEXT,
                $COL_CATEGORY TEXT NOT NULL
            )
        """.trimIndent()

        val createHistoryTable = """
            CREATE TABLE $TABLE_HISTORY (
                $COL_HIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_HIST_DATE INTEGER NOT NULL,
                $COL_HIST_SCORE INTEGER NOT NULL,
                $COL_HIST_TOTAL INTEGER NOT NULL,
                $COL_HIST_TIME INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createQuestionsTable)
        db.execSQL(createHistoryTable)

        // Seed default sample questions
        seedDefaultQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUESTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    private fun seedDefaultQuestions(db: SQLiteDatabase) {
        val sampleQuestions = listOf(
            Question(
                questionText = "Thủ đô của nước Cộng hòa Xã hội Chủ nghĩa Việt Nam là gì?",
                optionA = "Hà Nội",
                optionB = "Thành phố Hồ Chí Minh",
                optionC = "Đà Nẵng",
                optionD = "Huế",
                correctOptionIndex = 0,
                explanation = "Hà Nội là thủ đô của Việt Nam từ năm 1976 sau khi đất nước thống nhất.",
                category = "Địa lý"
            ),
            Question(
                questionText = "Kim loại nào có khả năng dẫn điện tốt nhất ở điều kiện tiêu chuẩn?",
                optionA = "Đồng (Cu)",
                optionB = "Vàng (Au)",
                optionC = "Bạc (Ag)",
                optionD = "Nhôm (Al)",
                correctOptionIndex = 2,
                explanation = "Bạc là kim loại dẫn điện và dẫn nhiệt tốt nhất trong tất cả các kim loại, tiếp theo là Đồng và Vàng.",
                category = "Khoa học"
            ),
            Question(
                questionText = "Ngôn ngữ lập trình chính thức và được Google khuyến nghị ưu tiên phát triển ứng dụng Android là gì?",
                optionA = "Java",
                optionB = "Kotlin",
                optionC = "C++",
                optionD = "Python",
                correctOptionIndex = 1,
                explanation = "Google đã chính thức công bố Kotlin là ngôn ngữ được ưu tiên hàng đầu (Kotlin-first) cho lập trình Android từ Google I/O 2019.",
                category = "Công nghệ"
            ),
            Question(
                questionText = "Đỉnh núi nào cao nhất thế giới so với mực nước biển?",
                optionA = "K2",
                optionB = "Fansipan",
                optionC = "Everest",
                optionD = "Kangchenjunga",
                correctOptionIndex = 2,
                explanation = "Đỉnh Everest nằm trên dãy Himalaya cao 8.848,86 m so với mực nước biển.",
                category = "Địa lý"
            ),
            Question(
                questionText = "Đơn vị nào dưới đây dùng để đo cường độ dòng điện trong hệ đo lường quốc tế (SI)?",
                optionA = "Vôn (V)",
                optionB = "Ampe (A)",
                optionC = "Ôm (Ω)",
                optionD = "Oát (W)",
                correctOptionIndex = 1,
                explanation = "Ampe (ký hiệu: A) là đơn vị đo cường độ dòng điện trong hệ SI.",
                category = "Khoa học"
            ),
            Question(
                questionText = "Bức họa nổi tiếng 'Mona Lisa' là kiệt tác của danh họa nào thời Phục Hưng?",
                optionA = "Vincent van Gogh",
                optionB = "Pablo Picasso",
                optionC = "Michelangelo",
                optionD = "Leonardo da Vinci",
                correctOptionIndex = 3,
                explanation = "Leonardo da Vinci đã vẽ kiệt tác Mona Lisa vào đầu thế kỷ 16.",
                category = "Nghệ thuật"
            ),
            Question(
                questionText = "Hành tinh nào có kích thước và khối lượng lớn nhất trong Hệ Mặt Trời?",
                optionA = "Sao Thổ",
                optionB = "Sao Mộc",
                optionC = "Sao Hải Vương",
                optionD = "Sao Hỏa",
                correctOptionIndex = 1,
                explanation = "Sao Mộc (Jupiter) là hành tinh lớn nhất trong Hệ Mặt Trời, có thể chứa hơn 1.300 Trái Đất bên trong.",
                category = "Thiên văn"
            ),
            Question(
                questionText = "Năm nào đánh dấu sự kiện Chủ tịch Hồ Chí Minh đọc bản Tuyên ngôn Độc lập tại Quảng trường Ba Đình?",
                optionA = "1930",
                optionB = "1945",
                optionC = "1954",
                optionD = "1975",
                correctOptionIndex = 1,
                explanation = "Vào ngày 2 tháng 9 năm 1945, Bác Hồ đã đọc Tuyên ngôn Độc lập khai sinh nước Việt Nam Dân chủ Cộng hòa.",
                category = "Lịch sử"
            )
        )

        for (q in sampleQuestions) {
            val values = ContentValues().apply {
                put(COL_QUESTION_TEXT, q.questionText)
                put(COL_OPTION_A, q.optionA)
                put(COL_OPTION_B, q.optionB)
                put(COL_OPTION_C, q.optionC)
                put(COL_OPTION_D, q.optionD)
                put(COL_CORRECT_OPTION, q.correctOptionIndex)
                put(COL_EXPLANATION, q.explanation)
                put(COL_CATEGORY, q.category)
            }
            db.insert(TABLE_QUESTIONS, null, values)
        }
    }

    // Question operations
    fun insertQuestion(question: Question): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_QUESTION_TEXT, question.questionText)
            put(COL_OPTION_A, question.optionA)
            put(COL_OPTION_B, question.optionB)
            put(COL_OPTION_C, question.optionC)
            put(COL_OPTION_D, question.optionD)
            put(COL_CORRECT_OPTION, question.correctOptionIndex)
            put(COL_EXPLANATION, question.explanation)
            put(COL_CATEGORY, question.category)
        }
        return db.insert(TABLE_QUESTIONS, null, values)
    }

    fun updateQuestion(question: Question): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_QUESTION_TEXT, question.questionText)
            put(COL_OPTION_A, question.optionA)
            put(COL_OPTION_B, question.optionB)
            put(COL_OPTION_C, question.optionC)
            put(COL_OPTION_D, question.optionD)
            put(COL_CORRECT_OPTION, question.correctOptionIndex)
            put(COL_EXPLANATION, question.explanation)
            put(COL_CATEGORY, question.category)
        }
        return db.update(TABLE_QUESTIONS, values, "$COL_ID = ?", arrayOf(question.id.toString()))
    }

    fun deleteQuestion(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_QUESTIONS, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun getAllQuestions(): List<Question> {
        val list = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_QUESTIONS,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val q = Question(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                    questionText = it.getString(it.getColumnIndexOrThrow(COL_QUESTION_TEXT)),
                    optionA = it.getString(it.getColumnIndexOrThrow(COL_OPTION_A)),
                    optionB = it.getString(it.getColumnIndexOrThrow(COL_OPTION_B)),
                    optionC = it.getString(it.getColumnIndexOrThrow(COL_OPTION_C)),
                    optionD = it.getString(it.getColumnIndexOrThrow(COL_OPTION_D)),
                    correctOptionIndex = it.getInt(it.getColumnIndexOrThrow(COL_CORRECT_OPTION)),
                    explanation = it.getString(it.getColumnIndexOrThrow(COL_EXPLANATION)) ?: "",
                    category = it.getString(it.getColumnIndexOrThrow(COL_CATEGORY)) ?: "Tổng hợp"
                )
                list.add(q)
            }
        }
        return list
    }

    fun getQuestionById(id: Long): Question? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_QUESTIONS,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return Question(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                    questionText = it.getString(it.getColumnIndexOrThrow(COL_QUESTION_TEXT)),
                    optionA = it.getString(it.getColumnIndexOrThrow(COL_OPTION_A)),
                    optionB = it.getString(it.getColumnIndexOrThrow(COL_OPTION_B)),
                    optionC = it.getString(it.getColumnIndexOrThrow(COL_OPTION_C)),
                    optionD = it.getString(it.getColumnIndexOrThrow(COL_OPTION_D)),
                    correctOptionIndex = it.getInt(it.getColumnIndexOrThrow(COL_CORRECT_OPTION)),
                    explanation = it.getString(it.getColumnIndexOrThrow(COL_EXPLANATION)) ?: "",
                    category = it.getString(it.getColumnIndexOrThrow(COL_CATEGORY)) ?: "Tổng hợp"
                )
            }
        }
        return null
    }

    fun resetDefaultQuestions() {
        val db = writableDatabase
        db.delete(TABLE_QUESTIONS, null, null)
        seedDefaultQuestions(db)
    }

    fun getQuestionsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_QUESTIONS", null)
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(0)
            }
        }
        return 0
    }

    // History operations
    fun insertHistory(score: Int, total: Int, timeSpentSeconds: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_HIST_DATE, System.currentTimeMillis())
            put(COL_HIST_SCORE, score)
            put(COL_HIST_TOTAL, total)
            put(COL_HIST_TIME, timeSpentSeconds)
        }
        return db.insert(TABLE_HISTORY, null, values)
    }

    fun getAllHistory(): List<QuizHistory> {
        val list = mutableListOf<QuizHistory>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_HISTORY,
            null,
            null,
            null,
            null,
            null,
            "$COL_HIST_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val item = QuizHistory(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_HIST_ID)),
                    dateEpochMillis = it.getLong(it.getColumnIndexOrThrow(COL_HIST_DATE)),
                    score = it.getInt(it.getColumnIndexOrThrow(COL_HIST_SCORE)),
                    totalQuestions = it.getInt(it.getColumnIndexOrThrow(COL_HIST_TOTAL)),
                    timeSpentSeconds = it.getInt(it.getColumnIndexOrThrow(COL_HIST_TIME))
                )
                list.add(item)
            }
        }
        return list
    }
}
