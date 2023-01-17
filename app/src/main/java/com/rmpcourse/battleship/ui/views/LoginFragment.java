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
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.databinding.FragmentLoginBinding;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private PlayerViewModel mPlayerViewModel;

    private boolean dataReady = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        binding.usernameEditText.setOnFocusChangeListener((view, b) -> {
            binding.usernameInputLayout.setError(null);
        });

        binding.passwordEditText.setOnFocusChangeListener((view, b) -> {
            binding.passwordInputLayout.setError(null);
        });

        binding.buttonSignIn.setOnClickListener(view -> {
            binding.usernameInputLayout.clearFocus();
            binding.passwordInputLayout.clearFocus();

            dataReady = true;
            String username = binding.usernameEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            /* TODO: заменить строковыми ресурсами */

            /**
             * Проверка имени пользователя
             */
            if (username.equals("")) {
                binding.usernameInputLayout.setError("Can't be empty!");
                dataReady = false;
            } else if (username.length() < 2) {
                binding.usernameInputLayout.setError("Min 2 characters!");
                dataReady = false;
            }

            /**
             * Проверка пароля
             */
            if (password.equals("")) {
                binding.passwordInputLayout.setError("Can't be empty!");
                dataReady = false;
            } else if (password.length() < 6) {
                binding.passwordInputLayout.setError("Min 6 characters!");
                dataReady = false;
            }

            if (!dataReady) return;

            if (isPlayerExists(username, password)) {
                NavDirections action = LoginFragmentDirections
                        .actionLoginFragmentToStartFragment(mPlayerViewModel.getPlayer().playerId);
                Navigation.findNavController(view).navigate(action);
            } else {
                Toast toast = Toast.makeText(getContext(),
                        "Пользователь с таким именем не зарегистрирован!",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private boolean isPlayerExists(String username, String password) {
        return mPlayerViewModel.findPlayerByUsernameAndPassword(username, password);
    }
}