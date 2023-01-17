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
import com.rmpcourse.battleship.data.leaderboard.Leaderboard;
import com.rmpcourse.battleship.data.player.Player;
import com.rmpcourse.battleship.databinding.FragmentRegisterBinding;
import com.rmpcourse.battleship.ui.viewmodel.LeaderboardViewModel;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private PlayerViewModel mPlayerViewModel;
    private LeaderboardViewModel mLeaderboardViewModel;
    private boolean dataReady = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);


        binding.usernameEditText.setOnFocusChangeListener((view, b) -> {
            binding.usernameInputLayout.setError(null);
        });

        binding.emailEditText.setOnFocusChangeListener(((view, b) -> {
            binding.emailInputLayout.setError(null);
        }));

        binding.passwordEditText.setOnFocusChangeListener((view, b) -> {
            binding.passwordInputLayout.setError(null);
        });

        binding.buttonSignUp.setOnClickListener(view -> {
            binding.usernameEditText.clearFocus();
            binding.emailEditText.clearFocus();
            binding.passwordEditText.clearFocus();


            String username = binding.usernameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            dataReady = true;

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
             * Проверка почты
             */
            if (email.equals("")) {
                binding.emailInputLayout.setError("Can't be empty!");
                dataReady = false;
            } else if (!validate(email)) {
                binding.emailInputLayout.setError("Invalid email address!");
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

            if (!isPlayerExists(username)) {
                Player player = new Player();
                player.username = username;
                player.password = password;
                player.email = email;

                long playerId = mPlayerViewModel.insert(player);

                Leaderboard leaderboard = new Leaderboard();
                leaderboard.playerLeaderboardId = playerId;
                leaderboard.playerUsername = username;
                mLeaderboardViewModel.insert(leaderboard);

                NavDirections action = RegisterFragmentDirections
                        .actionRegisterFragmentToMainScreenFragment();
                Navigation.findNavController(view).navigate(action);

                /* TODO: добавить строковый ресурс */
                Toast toast = Toast.makeText(getContext(),
                        "Пользователь успешно зарегистрирован!",
                        Toast.LENGTH_LONG);
                toast.show();

            } else {

                /* TODO: добавить строковый ресурс */
                Toast toast = Toast.makeText(getContext(),
                        "Пользователь с таким именем уже существует!",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    // Регулярное выражение для проверки почты
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    // Валидация почты
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private boolean isPlayerExists(String username) {
        return mPlayerViewModel.findPlayerByUsername(username);
    }
}