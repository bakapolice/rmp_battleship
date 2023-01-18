package com.rmpcourse.battleship.ui.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.databinding.ItemLeaderboardBinding;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    private final ItemLeaderboardBinding binding;

    public LeaderboardViewHolder(ItemLeaderboardBinding b) {
        super(b.getRoot());
        this.binding = b;
    }

    public void bind(Leaderboard leaderboard, LeaderboardViewHolder viewHolder) {
        binding.textViewPlayerUsername.setText(leaderboard.playerUsername);
        binding.textViewWin.setText(viewHolder.itemView.getContext().getString(R.string.wins_count, leaderboard.totalWins));
        binding.textViewLoss.setText(viewHolder.itemView.getContext().getString(R.string.losses_count, leaderboard.totalLosses));
    }
}
