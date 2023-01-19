package com.rmpcourse.battleship.ui.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.databinding.ItemLeaderboardBinding;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    private final ItemLeaderboardBinding binding;

    //Вложенный субкласс RecyclerView.ViewHolder используется для реализации
    //паттерна View-Holder в контексте RecyclerView
    public LeaderboardViewHolder(ItemLeaderboardBinding b) {
        super(b.getRoot());
        this.binding = b;
    }

    // Установка данных для отображения
    public void bind(Leaderboard leaderboard, LeaderboardViewHolder viewHolder) {
        binding.textViewPlayerUsername.setText(leaderboard.playerUsername);
        binding.textViewWin.setText(viewHolder.itemView.getContext().getString(R.string.wins_count, leaderboard.totalWins));
        binding.textViewLoss.setText(viewHolder.itemView.getContext().getString(R.string.losses_count, leaderboard.totalLosses));
    }
}
