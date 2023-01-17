package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMatchBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        long playerId = StartFragmentArgs.fromBundle(getArguments()).getPlayerId();
        mPlayerViewModel.findPlayerById(playerId);
        binding.currentPlayer.setText(mPlayerViewModel.getPlayer().username);

        binding.targetPlayerEditText.setOnFocusChangeListener((view, b) -> {
            binding.targetPlayerTextView.setError(null);
        });

        binding.buttonFindTargetPlayer.setOnClickListener(view -> {
            dataReady = true;
            binding.targetPlayerEditText.clearFocus();
            String targetUsername = binding.targetPlayerEditText.getText().toString();

            /**
             * Проверка имени пользователя
             */
            if (targetUsername.equals("")) {

                /* TODO: заменить строковыми ресурсами */
                binding.targetPlayerTextView.setError("Can't be empty!");
                dataReady = false;
            } else if (targetUsername.length() < 2) {
                binding.targetPlayerTextView.setError("Min 2 characters!");
                dataReady = false;
            }

            if(!dataReady) return;

            if(mPlayerViewModel.findTargetPlayerByUsername(targetUsername)){
                if(mPlayerViewModel.getTargetPlayer().playerId == mPlayerViewModel.getPlayer().playerId){
                    Toast toast = Toast.makeText(getContext(),
                            "Вы не можете играть сами с собой!",
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                PlayerAndLeaderboard leaderboard = mLeaderboardViewModel.getPlayerAndLeaderboardByPlayerId(mPlayerViewModel.getTargetPlayer().playerId);
                /* TODO: заменить на форматированную строку ресурсов */
                binding.targetPlayerTextView.setText(leaderboard.player.username + ". Wins: " + leaderboard.leaderboard.totalWins + ". Losses: " +
                        leaderboard.leaderboard.totalLosses);
                binding.buttonStartGame.setEnabled(true);
            }
            else {
                binding.buttonStartGame.setEnabled(false);
                Toast toast = Toast.makeText(getContext(),
                        "Пользователь с таким именем не зарегистрирован!",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        binding.buttonStartGame.setOnClickListener(view -> {
            NavDirections action = MatchFragmentDirections
                    .actionMatchFragmentToInGameFragment(mPlayerViewModel.getPlayer().playerId,
                            mPlayerViewModel.getTargetPlayer().playerId);
            Navigation.findNavController(view).navigate(action);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}