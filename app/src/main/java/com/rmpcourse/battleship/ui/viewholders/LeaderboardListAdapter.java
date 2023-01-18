package com.rmpcourse.battleship.ui.viewholders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.databinding.ItemLeaderboardBinding;

public class LeaderboardListAdapter extends ListAdapter<Leaderboard, LeaderboardViewHolder> {
    public LeaderboardListAdapter(@NonNull DiffUtil.ItemCallback<Leaderboard> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeaderboardViewHolder(ItemLeaderboardBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Leaderboard current = getItem(position);
        holder.bind(current);
    }

    public static class LeaderboardDiff extends DiffUtil.ItemCallback<Leaderboard> {

        @Override
        public boolean areItemsTheSame(@NonNull Leaderboard oldItem, @NonNull Leaderboard newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Leaderboard oldItem, @NonNull Leaderboard newItem) {
            return oldItem.equals(newItem);
        }
    }
}
