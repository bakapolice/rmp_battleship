package com.rmpcourse.battleship.ui.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.databinding.ItemLeaderboardBinding;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    private final ItemLeaderboardBinding binding;

    public LeaderboardViewHolder(ItemLeaderboardBinding b) {
        super(b.getRoot());
        this.binding = b;
    }

    public void bind(Leaderboard leaderboard) {
        binding.textViewPlayerUsername.setText(leaderboard.playerUsername);
        binding.textViewWin.setText("Total wins: " + leaderboard.totalWins);
        binding.textViewLoss.setText("Total losses: " + leaderboard.totalLosses);
    }
}
