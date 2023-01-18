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

        binding = FragmentMatchBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        long playerId = StartFragmentArgs.fromBundle(getArguments()).getPlayerId();
        mPlayerViewModel.findPlayerById(playerId);
        binding.currentPlayer.setText(mPlayerViewModel.getPlayer().username);

        binding.targetPlayerEditText.setOnFocusChangeListener((view, b) ->
                binding.targetPlayerInputLayout.setError(null));

        binding.targetPlayerEditText.setOnFocusChangeListener((view, b) ->
                binding.targetPlayerTextView.setError(null));

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

            if (!dataReady) return;

            if (mPlayerViewModel.findTargetPlayerByUsername(targetUsername)) {
                if (mPlayerViewModel.getTargetPlayer().playerId == mPlayerViewModel.getPlayer().playerId) {
                    Toast toast = Toast.makeText(getContext(),
                            getString(R.string.play_with_yourself),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                PlayerAndLeaderboard leaderboard = mLeaderboardViewModel.getPlayerAndLeaderboardByPlayerId(mPlayerViewModel.getTargetPlayer().playerId);
                binding.targetPlayerTextView.setVisibility(View.VISIBLE);
                binding.targetPlayerTextView.setText(getString(R.string.target_player_info,
                        leaderboard.player.username, leaderboard.leaderboard.totalWins,
                        leaderboard.leaderboard.totalLosses));
                binding.buttonStartGame.setEnabled(true);
            } else {
                binding.buttonStartGame.setEnabled(false);
                Toast toast = Toast.makeText(getContext(),
                        getString(R.string.user_not_registered),
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