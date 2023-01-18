package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentLeaderboardBinding;
import com.rmpcourse.battleship.ui.viewholders.LeaderboardListAdapter;
import com.rmpcourse.battleship.ui.viewmodel.LeaderboardViewModel;

public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;
    private LeaderboardViewModel mLeaderboardViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        RecyclerView recyclerView = binding.leaderBoardRecyclerView;

        // Создание и назначение адаптера
        final LeaderboardListAdapter adapter = new LeaderboardListAdapter(new LeaderboardListAdapter.LeaderboardDiff());
        recyclerView.setAdapter(adapter);

        //recyclerView выводит элементы в вертикальном списке
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        mLeaderboardViewModel.getAllLeaderboards().observe(getViewLifecycleOwner(), adapter::submitList);

        //Присоединение ItemDecorator для вывода разделителей
        recyclerView.addItemDecoration(
                new LeaderboardItemDivider(getActivity(), R.drawable.divider));

        //Улучшает быстродействие, если размер макета RecyclerView не изменяется
        recyclerView.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}