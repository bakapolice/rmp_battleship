package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.databinding.FragmentScoresBinding;
import com.rmpcourse.battleship.ui.viewholders.ScoreListAdapter;
import com.rmpcourse.battleship.ui.viewmodel.ScoresViewModel;

public class ScoresFragment extends Fragment {

    private FragmentScoresBinding binding;
    private ScoresViewModel mScoresViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Создаем объект представления
        binding = FragmentScoresBinding.inflate(inflater, container, false);

        // Получаем аргументы на данном фрагменте
        long playerId = ScoresFragmentArgs.fromBundle(getArguments()).getPlayerId();

        // получение ссылки на RecyclerView
        RecyclerView recyclerView = binding.recyclerViewScores;

        // Создание и назначение адаптера
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.ScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Получаем экземплят модели данных
        mScoresViewModel = new ViewModelProvider(this).get(ScoresViewModel.class);
        // Ищем игрока по id
        mScoresViewModel.findPlayerWithScoresById(playerId);
        // Подписываемся на список нужных для вывода данных и выводим их в recyclerView
        mScoresViewModel.getPlayerWithScores().observe(getViewLifecycleOwner(),
                player -> adapter.submitList(player.scores));

        // Устанавливаем представление для фрагмента
        return binding.getRoot();
    }
}