package com.example.tracnghiemapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.R;
import com.example.tracnghiemapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface OnQuestionActionListener {
        void onEdit(Question question);
        void onDelete(Question question);
    }

    private final Context context;
    private List<Question> questionList = new ArrayList<>();
    private final OnQuestionActionListener listener;

    public QuestionAdapter(Context context, OnQuestionActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setQuestions(List<Question> questions) {
        this.questionList = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.tvCategory.setText(q.getCategory());
        holder.tvQuestionText.setText((position + 1) + ". " + q.getQuestionText());

        holder.tvOptA.setText("A. " + q.getOptionA());
        holder.tvOptB.setText("B. " + q.getOptionB());
        holder.tvOptC.setText("C. " + q.getOptionC());
        holder.tvOptD.setText("D. " + q.getOptionD());

        // Reset backgrounds
        holder.tvOptA.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptB.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptC.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptD.setBackgroundResource(R.drawable.bg_option_default);

        // Highlight correct option
        switch (q.getCorrectOptionIndex()) {
            case 0:
                holder.tvOptA.setBackgroundResource(R.drawable.bg_option_correct);
                break;
            case 1:
                holder.tvOptB.setBackgroundResource(R.drawable.bg_option_correct);
                break;
            case 2:
                holder.tvOptC.setBackgroundResource(R.drawable.bg_option_correct);
                break;
            case 3:
                holder.tvOptD.setBackgroundResource(R.drawable.bg_option_correct);
                break;
        }

        if (q.getExplanation() != null && !q.getExplanation().trim().isEmpty()) {
            holder.tvExplanation.setVisibility(View.VISIBLE);
            holder.tvExplanation.setText("Giải thích: " + q.getExplanation());
        } else {
            holder.tvExplanation.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(q);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(q);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvQuestionText, tvOptA, tvOptB, tvOptC, tvOptD, tvExplanation;
        ImageView btnEdit, btnDelete;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvItemCategory);
            tvQuestionText = itemView.findViewById(R.id.tvItemQuestionText);
            tvOptA = itemView.findViewById(R.id.tvItemOptA);
            tvOptB = itemView.findViewById(R.id.tvItemOptB);
            tvOptC = itemView.findViewById(R.id.tvItemOptC);
            tvOptD = itemView.findViewById(R.id.tvItemOptD);
            tvExplanation = itemView.findViewById(R.id.tvItemExplanation);
            btnEdit = itemView.findViewById(R.id.btnEditQuestion);
            btnDelete = itemView.findViewById(R.id.btnDeleteQuestion);
        }
    }
}
