package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.rmpcourse.battleship.databinding.FragmentStartBinding;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

public class StartFragment extends Fragment {

    private FragmentStartBinding binding;
    private PlayerViewModel mPlayerViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStartBinding.inflate(inflater, container, false);

        // Получаем экземпляр модели данных игрока
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        assert getArguments() != null;

        // Получаем аргументы, переданные во фрагмент
        long playerId = StartFragmentArgs.fromBundle(getArguments()).getPlayerId();
        // Вызываем метод в модели данных, чтобы найти и сохранить информацию о текущем игроке
        mPlayerViewModel.findPlayerById(playerId);

        // Выводим имя автризованного пользователя
        binding.textViewCurrentUser.setText(mPlayerViewModel.getPlayer().username);

        // Кнопка "Играть"
        binding.buttonPlay.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToMatchFragment(mPlayerViewModel.getPlayer().playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Кнопка "Результаты"
        binding.buttonScores.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToScoresFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Кнопка "Таблица лидеров"
        binding.buttonLeaderboard.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToLeaderboardFragment();
            Navigation.findNavController(view).navigate(action);
        });

        // Кнопка "Выйти"
        binding.buttonLogout.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections.actionGlobalMainScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}