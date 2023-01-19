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

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.score.Score;
import com.rmpcourse.battleship.databinding.FragmentResultsBinding;
import com.rmpcourse.battleship.ui.viewmodel.LeaderboardViewModel;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;
import com.rmpcourse.battleship.ui.viewmodel.ScoresViewModel;

import java.util.Date;

public class ResultsFragment extends Fragment {

    // инструмент, который позволяет проще писать код для взаимодейтсвия с view
    // используя декларативный формат,
    private FragmentResultsBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private ScoresViewModel mScoresViewModel;
    private LeaderboardViewModel mLeaderboardViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Создаем объект представления
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        // Получаем экземпляры модели данных
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mScoresViewModel = new ViewModelProvider(this).get(ScoresViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        // Получаем аргументы
        long playerId = ResultsFragmentArgs.fromBundle(getArguments()).getPlayerId();
        long targetPlayerId = ResultsFragmentArgs.fromBundle(getArguments()).getTargetPlayerId();
        // Вывзываем методы в модели данных для поиска данных в БД
        mPlayerViewModel.findPlayerById(playerId);
        mPlayerViewModel.findTargetPlayerById(targetPlayerId);

        // Получаем аргументы
        int matchTime = ResultsFragmentArgs.fromBundle(getArguments()).getMatchTime();
        boolean win = ResultsFragmentArgs.fromBundle(getArguments()).getWin();
        long today = new Date().getTime();

        // Создаем новые объекты и устанавливаем в них необходимые данные
        Score playerScore = new Score();
        playerScore.playerUsername = mPlayerViewModel.getPlayer().username;
        playerScore.targetUsername = mPlayerViewModel.getTargetPlayer().username;
        playerScore.playerScoreId = mPlayerViewModel.getPlayer().playerId;

        playerScore.date = today;
        playerScore.matchTime = matchTime;

        Score targetScore = new Score();
        targetScore.playerUsername = mPlayerViewModel.getTargetPlayer().username;
        targetScore.targetUsername = mPlayerViewModel.getPlayer().username;
        targetScore.playerScoreId = mPlayerViewModel.getTargetPlayer().playerId;

        targetScore.date = today;
        targetScore.matchTime = matchTime;

        Leaderboard playerLeaderboard = mLeaderboardViewModel.findLeaderboardByPlayerId(playerId);
        Leaderboard targetLeaderboard = mLeaderboardViewModel.findLeaderboardByPlayerId(targetPlayerId);

        if (win) {
            binding.winnerTextView.setText(getString(R.string.you_win));
            binding.looserTextView.setText(getString(R.string.other_player_lose, mPlayerViewModel.getTargetPlayer().username));

            playerScore.matchResult = getString(R.string.win);
            targetScore.matchResult = getString(R.string.loss);

            playerLeaderboard.totalWins = playerLeaderboard.totalWins + 1;
            targetLeaderboard.totalLosses = targetLeaderboard.totalLosses + 1;

        } else {

            playerScore.matchResult = getString(R.string.loss);
            targetScore.matchResult = getString(R.string.win);

            binding.winnerTextView.setText(getString(R.string.you_lose));
            binding.looserTextView.setText(getString(R.string.other_player_won, mPlayerViewModel.getTargetPlayer().username));

            targetLeaderboard.totalWins = targetLeaderboard.totalWins + 1;
            playerLeaderboard.totalLosses = playerLeaderboard.totalLosses + 1;
        }

        // С помощью модели данных записываем новые значения и обновляем существующие в БД
        mScoresViewModel.insert(playerScore);
        mScoresViewModel.insert(targetScore);
        mLeaderboardViewModel.update(playerLeaderboard);
        mLeaderboardViewModel.update(targetLeaderboard);

        // Кнопка "Играть снова"
        binding.buttonPlayAgain.setOnClickListener(view -> {
            NavDirections action = ResultsFragmentDirections
                    .actionResultsFragmentToInGameFragment(playerId, targetPlayerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Кнопка "Вернуться в меню"
        binding.buttonReturnToMenu.setOnClickListener(view -> {
            NavDirections action = ResultsFragmentDirections
                    .actionResultsFragmentToStartFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Устанавливаем представление для фрагмента
        return binding.getRoot();
    }
}