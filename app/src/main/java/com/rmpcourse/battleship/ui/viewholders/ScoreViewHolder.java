package com.rmpcourse.battleship.ui.viewholders;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.databinding.ItemScoreBinding;

import java.text.SimpleDateFormat;

public class ScoreViewHolder extends RecyclerView.ViewHolder {
    private final ItemScoreBinding binding;

    public ScoreViewHolder(ItemScoreBinding b) {
        super(b.getRoot());
        this.binding = b;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    public void bind(Score score, ScoreViewHolder viewHolder) {
        binding.textViewTargetUsername.setText(score.targetUsername);
        binding.textViewMatchDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(score.date));
        binding.textViewMatchTime.setText(viewHolder.itemView.getContext().getString(R.string.match_time, score.matchTime));
        binding.textViewMatchResult.setText(score.matchResult);
    }
}

