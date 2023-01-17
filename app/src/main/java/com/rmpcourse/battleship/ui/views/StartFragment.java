package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentStartBinding;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

public class StartFragment extends Fragment {

   private FragmentStartBinding binding;
   private PlayerViewModel mPlayerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* TODO: запретить перемещаться на предыдущий экран регистрации или авторизации в навграфе*/
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        assert getArguments() != null;
        long playerId = StartFragmentArgs.fromBundle(getArguments()).getPlayerId();
        mPlayerViewModel.findPlayerById(playerId);

        binding.textViewCurrentUser.setText(mPlayerViewModel.getPlayer().username);

        binding.buttonPlay.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToMatchFragment(mPlayerViewModel.getPlayer().playerId);
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonScores.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToScoresFragment(playerId);
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonLeaderboard.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToLeaderboardFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonLogout.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections.actionGlobalMainScreenFragment();
            Navigation.findNavController(view).navigate(action);
        });





        /*TODO: make global actions to start framgent from ingame and results fragment */
        /*TODO: logout button */
        /*TODO: popups to*/
        /* TODO: popup on backpress and logout button */

        return binding.getRoot();
    }
}