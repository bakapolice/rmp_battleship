package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.buttonSignUp.setOnClickListener(view -> {
            NavDirections action = RegisterFragmentDirections
                    .actionRegisterFragmentToStartFragment();
            Navigation.findNavController(view).navigate(action);
        });

        return binding.getRoot();
    }
}