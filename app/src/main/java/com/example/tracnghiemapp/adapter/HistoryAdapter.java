package com.example.tracnghiemapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.R;
import com.example.tracnghiemapp.model.QuizHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private List<QuizHistory> historyList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public void setHistoryList(List<QuizHistory> list) {
        this.historyList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizHistory item = historyList.get(position);
        int percentage = item.getPercentage();
        holder.tvPercentage.setText(percentage + "%");
        holder.tvScore.setText("Đúng " + item.getScore() + "/" + item.getTotalQuestions() + " câu");
        holder.tvDate.setText(dateFormat.format(new Date(item.getDateMillis())));

        int min = item.getTimeSpentSeconds() / 60;
        int sec = item.getTimeSpentSeconds() % 60;
        holder.tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));

        if (percentage >= 80) {
            holder.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.quiz_green));
        } else if (percentage >= 50) {
            holder.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.quiz_yellow));
        } else {
            holder.tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.quiz_red));
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvPercentage, tvScore, tvDate, tvTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPercentage = itemView.findViewById(R.id.tvHistPercentage);
            tvScore = itemView.findViewById(R.id.tvHistScore);
            tvDate = itemView.findViewById(R.id.tvHistDate);
            tvTime = itemView.findViewById(R.id.tvHistTime);
        }
    }
}
