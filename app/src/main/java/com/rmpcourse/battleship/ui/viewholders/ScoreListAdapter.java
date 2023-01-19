package com.rmpcourse.battleship.ui.viewholders;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.databinding.ItemScoreBinding;

public class ScoreListAdapter extends ListAdapter<Score, ScoreViewHolder> {

    // Конструктор адаптера, который вызывает коллбэк на проверку списков на отличия
    public ScoreListAdapter(@NonNull DiffUtil.ItemCallback<Score> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScoreViewHolder(ItemScoreBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }


    // Установка данных для отображения
    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score current = getItem(position);
        holder.bind(current, holder);
    }

    // Проверка списков на изменения
    public static class ScoreDiff extends DiffUtil.ItemCallback<Score> {

        @Override
        public boolean areItemsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Score oldItem, @NonNull Score newItem) {
            return oldItem.equals(newItem);
        }
    }
}
