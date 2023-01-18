package com.rmpcourse.battleship.ui.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.rmpcourse.battleship.databinding.FragmentMainScreenBinding;

public class MainScreenFragment extends Fragment {

    private FragmentMainScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainScreenBinding.inflate(inflater, container, false);

        binding.buttonLogin.setOnClickListener(view -> {
            NavDirections action = MainScreenFragmentDirections
                    .actionMainScreenFragmentToLoginFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonRegister.setOnClickListener(view -> {
            NavDirections action = MainScreenFragmentDirections
                    .actionMainScreenFragmentToRegisterFragment();
            Navigation.findNavController(view).navigate(action);
        });

        binding.buttonExit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }

}