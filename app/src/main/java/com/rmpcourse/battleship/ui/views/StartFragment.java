package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {

   private FragmentStartBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false);

        binding.buttonPlay.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToMatchFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonScores.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToScoresFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonLeaderboard.setOnClickListener(view -> {
            NavDirections action = StartFragmentDirections
                    .actionStartFragmentToLeaderboardFragment();
            Navigation.findNavController(view).navigate(action);
        });

        return binding.getRoot();
    }
}