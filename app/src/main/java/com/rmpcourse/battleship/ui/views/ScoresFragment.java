package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.databinding.FragmentScoresBinding;
import com.rmpcourse.battleship.ui.viewholders.ScoreListAdapter;
import com.rmpcourse.battleship.ui.viewmodel.ScoresViewModel;

import java.util.List;

public class ScoresFragment extends Fragment {

    private FragmentScoresBinding binding;
    private ScoresViewModel mScoresViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScoresBinding.inflate(inflater, container, false);

        long playerId = ScoresFragmentArgs.fromBundle(getArguments()).getPlayerId();

        RecyclerView recyclerView = binding.recyclerViewScores;
        final ScoreListAdapter adapter = new ScoreListAdapter(new ScoreListAdapter.ScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mScoresViewModel = new ViewModelProvider(this).get(ScoresViewModel.class);
        mScoresViewModel.findPlayerWithScoresById(playerId);
        mScoresViewModel.getPlayerWithScores().observe(getViewLifecycleOwner(),
                player -> adapter.submitList(player.scores));

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}