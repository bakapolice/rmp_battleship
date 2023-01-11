package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentMainScreenBinding;

public class MainScreenFragment extends Fragment {

    private FragmentMainScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainScreenBinding.inflate(inflater, container, false);
        binding.buttonScores.setOnClickListener(view -> {
            NavDirections action = MainScreenFragmentDirections
                    .actionMainScreenFragmentToScoresFragment();
            Navigation.findNavController(view).navigate(action);
        });

        return binding.getRoot();
    }
}