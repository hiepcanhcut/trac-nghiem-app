package com.example.tracnghiemapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracnghiemapp.R;
import com.example.tracnghiemapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context context;
    private final List<Question> questionList;
    private final int[] userAnswers;

    public ReviewAdapter(Context context, List<Question> questionList, int[] userAnswers) {
        this.context = context;
        this.questionList = questionList != null ? questionList : new ArrayList<>();
        this.userAnswers = userAnswers;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_question, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Question q = questionList.get(position);
        int userChoice = (userAnswers != null && position < userAnswers.length) ? userAnswers[position] : -1;
        boolean isCorrect = (userChoice == q.getCorrectOptionIndex());

        holder.tvQuestionNumber.setText("Câu " + (position + 1) + ": " + q.getCategory());
        holder.tvQuestionText.setText(q.getQuestionText());

        if (isCorrect) {
            holder.ivStatus.setImageResource(android.R.drawable.checkbox_on_background);
            holder.ivStatus.setColorFilter(ContextCompat.getColor(context, R.color.quiz_green));
        } else {
            holder.ivStatus.setImageResource(android.R.drawable.ic_delete);
            holder.ivStatus.setColorFilter(ContextCompat.getColor(context, R.color.quiz_red));
        }

        // Set options text
        holder.tvOptA.setText("A. " + q.getOptionA());
        holder.tvOptB.setText("B. " + q.getOptionB());
        holder.tvOptC.setText("C. " + q.getOptionC());
        holder.tvOptD.setText("D. " + q.getOptionD());

        // Reset backgrounds
        holder.tvOptA.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptB.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptC.setBackgroundResource(R.drawable.bg_option_default);
        holder.tvOptD.setBackgroundResource(R.drawable.bg_option_default);

        TextView[] optionViews = new TextView[]{holder.tvOptA, holder.tvOptB, holder.tvOptC, holder.tvOptD};

        // If user chose something and it was wrong, mark user choice red
        if (userChoice >= 0 && userChoice < 4 && userChoice != q.getCorrectOptionIndex()) {
            optionViews[userChoice].setBackgroundResource(R.drawable.bg_option_wrong);
            optionViews[userChoice].append("  (Bạn đã chọn ✗)");
        }

        // Mark correct answer green
        int correctIdx = q.getCorrectOptionIndex();
        if (correctIdx >= 0 && correctIdx < 4) {
            optionViews[correctIdx].setBackgroundResource(R.drawable.bg_option_correct);
            optionViews[correctIdx].append("  (Đáp án đúng ✓)");
        }

        // Explanation
        if (q.getExplanation() != null && !q.getExplanation().trim().isEmpty()) {
            holder.layoutExplanation.setVisibility(View.VISIBLE);
            holder.tvExplanation.setText("Giải thích: " + q.getExplanation());
        } else {
            holder.layoutExplanation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStatus;
        TextView tvQuestionNumber, tvQuestionText;
        TextView tvOptA, tvOptB, tvOptC, tvOptD;
        LinearLayout layoutExplanation;
        TextView tvExplanation;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.ivReviewStatus);
            tvQuestionNumber = itemView.findViewById(R.id.tvReviewQuestionNumber);
            tvQuestionText = itemView.findViewById(R.id.tvReviewQuestionText);
            tvOptA = itemView.findViewById(R.id.tvReviewOptA);
            tvOptB = itemView.findViewById(R.id.tvReviewOptB);
            tvOptC = itemView.findViewById(R.id.tvReviewOptC);
            tvOptD = itemView.findViewById(R.id.tvReviewOptD);
            layoutExplanation = itemView.findViewById(R.id.layoutExplanation);
            tvExplanation = itemView.findViewById(R.id.tvReviewExplanation);
        }
    }
}
