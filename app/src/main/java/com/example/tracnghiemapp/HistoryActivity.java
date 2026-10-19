package com.example.tracnghiemapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.adapter.HistoryAdapter;
import com.example.tracnghiemapp.database.QuizDbHelper;
import com.example.tracnghiemapp.model.QuizHistory;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private QuizDbHelper dbHelper;
    private HistoryAdapter adapter;
    private RecyclerView rvHistory;
    private LinearLayout layoutEmptyHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new QuizDbHelper(this);

        initViews();
        loadHistory();
    }

    private void initViews() {
        Toolbar toolbarHistory = findViewById(R.id.toolbarHistory);
        toolbarHistory.setNavigationOnClickListener(v -> finish());

        rvHistory = findViewById(R.id.rvHistory);
        layoutEmptyHistory = findViewById(R.id.layoutEmptyHistory);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this);
        rvHistory.setAdapter(adapter);
    }

    private void loadHistory() {
        List<QuizHistory> list = dbHelper.getAllHistory();
        adapter.setHistoryList(list);

        if (list.isEmpty()) {
            layoutEmptyHistory.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            layoutEmptyHistory.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
        }
    }
}
