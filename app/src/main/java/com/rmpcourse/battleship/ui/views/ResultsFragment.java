package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private FragmentResultsBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private ScoresViewModel mScoresViewModel;
    private LeaderboardViewModel mLeaderboardViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mScoresViewModel = new ViewModelProvider(this).get(ScoresViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        long playerId = ResultsFragmentArgs.fromBundle(getArguments()).getPlayerId();
        long targetPlayerId = ResultsFragmentArgs.fromBundle(getArguments()).getTargetPlayerId();
        mPlayerViewModel.findPlayerById(playerId);
        mPlayerViewModel.findTargetPlayerById(targetPlayerId);

        int matchTime = ResultsFragmentArgs.fromBundle(getArguments()).getMatchTime();
        boolean win = ResultsFragmentArgs.fromBundle(getArguments()).getWin();
        long today = new Date().getTime();

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
            binding.imageTrophy.setVisibility(View.VISIBLE);
            binding.winnerTextView.setText(getString(R.string.you_win));

            playerScore.matchResult = "WIN";
            targetScore.matchResult = "LOSS";

            playerLeaderboard.totalWins = playerLeaderboard.totalWins + 1;
            targetLeaderboard.totalLosses = targetLeaderboard.totalLosses + 1;

        } else {

            playerScore.matchResult = "LOSS";
            targetScore.matchResult = "WIN";

            binding.imageTrophy.setVisibility(View.GONE);
            binding.winnerTextView.setText(getString(R.string.you_lose));

            targetLeaderboard.totalWins = targetLeaderboard.totalWins + 1;
            playerLeaderboard.totalLosses = playerLeaderboard.totalLosses + 1;
        }

        mScoresViewModel.insert(playerScore);
        mScoresViewModel.insert(targetScore);
        mLeaderboardViewModel.update(playerLeaderboard);
        mLeaderboardViewModel.update(targetLeaderboard);

        binding.buttonPlayAgain.setOnClickListener(view -> {
            NavDirections action = ResultsFragmentDirections
                    .actionResultsFragmentToInGameFragment(playerId, targetPlayerId);
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonReturnToMenu.setOnClickListener(view -> {
            NavDirections action = ResultsFragmentDirections
                    .actionResultsFragmentToStartFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}