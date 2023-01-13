package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rmpcourse.battleship.R;
import com.rmpcourse.battleship.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.buttonSignIn.setOnClickListener(view -> {
            NavDirections action = LoginFragmentDirections
                    .actionLoginFragmentToStartFragment();
            Navigation.findNavController(view).navigate(action);
        });

        return binding.getRoot();
    }
}