package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.data.player.PlayerAndLeaderboard;
import com.rmpcourse.battleship.databinding.FragmentMatchBinding;
import com.rmpcourse.battleship.ui.viewmodel.LeaderboardViewModel;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

public class MatchFragment extends Fragment {
    private FragmentMatchBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private LeaderboardViewModel mLeaderboardViewModel;
    private boolean dataReady = true;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Создаем объект представления
        binding = FragmentMatchBinding.inflate(inflater, container, false);

        // Получаем экземплярые моделей данных
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        //Получаем аргументы
        long playerId = StartFragmentArgs.fromBundle(getArguments()).getPlayerId();

        // С помощью методов модели данных находим в БД нужную информацию
        mPlayerViewModel.findPlayerById(playerId);

        // Устанавливаем имя текущего авторизованного пользователя
        binding.currentPlayer.setText(mPlayerViewModel.getPlayer().username);

        //  Вешаем слушатели на поля ввода
        binding.targetPlayerEditText.setOnFocusChangeListener((view, b) ->
                binding.targetPlayerInputLayout.setError(null));
        binding.targetPlayerEditText.setOnFocusChangeListener((view, b) ->
                binding.targetPlayerTextView.setError(null));

        // Кнопка "Найти"
        binding.buttonFindTargetPlayer.setOnClickListener(view -> {
            dataReady = true;
            binding.targetPlayerEditText.clearFocus();
            String targetUsername = binding.targetPlayerEditText.getText().toString();

            /**
             * Проверка имени пользователя
             */
            if (targetUsername.equals("")) {
                binding.targetPlayerInputLayout.setError(getString(R.string.empty_field_error));
                dataReady = false;
            } else if (targetUsername.length() < 2) {
                binding.targetPlayerInputLayout.setError(getString(R.string.min_length_username_field_error));
                dataReady = false;
            }

            // Если все поля не заполнены, то не продолжаем
            if (!dataReady) return;


            // Ищем через модель данных нужную информацию, если она есть - продолжаем
            if (mPlayerViewModel.findTargetPlayerByUsername(targetUsername)) {
                // С помощью данные, хранящихся в модели данных, проверяем какого противника мы нашли
                if (mPlayerViewModel.getTargetPlayer().playerId == mPlayerViewModel.getPlayer().playerId) {
                   // Выводим всплывающее сообщение
                    Toast toast = Toast.makeText(getContext(),
                            getString(R.string.play_with_yourself),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                // Получаем информацию о противнике из модели данных
                PlayerAndLeaderboard leaderboard = mLeaderboardViewModel.getPlayerAndLeaderboardByPlayerId(mPlayerViewModel.getTargetPlayer().playerId);
                binding.targetPlayerTextView.setVisibility(View.VISIBLE);
                // Устанваливаем информацию о противнике в текстовую строку
                binding.targetPlayerTextView.setText(getString(R.string.target_player_info,
                        leaderboard.player.username, leaderboard.leaderboard.totalWins,
                        leaderboard.leaderboard.totalLosses));
                binding.buttonStartGame.setEnabled(true);
            } else {
                binding.buttonStartGame.setEnabled(false);

                // Показываем всплывающее сообщение
                Toast toast = Toast.makeText(getContext(),
                        getString(R.string.user_not_registered),
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Кнопка "Начать"
        binding.buttonStartGame.setOnClickListener(view -> {
            NavDirections action = MatchFragmentDirections
                    .actionMatchFragmentToInGameFragment(mPlayerViewModel.getPlayer().playerId,
                            mPlayerViewModel.getTargetPlayer().playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Устанавливаем представление для фрагмента
        return binding.getRoot();
    }
}