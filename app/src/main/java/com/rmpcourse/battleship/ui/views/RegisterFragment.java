package com.rmpcourse.battleship.ui.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        mLeaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);


        binding.usernameEditText.setOnFocusChangeListener((view, b) -> binding.usernameInputLayout.setError(null));

        binding.emailEditText.setOnFocusChangeListener(((view, b) -> binding.emailInputLayout.setError(null)));

        binding.passwordEditText.setOnFocusChangeListener((view, b) -> binding.passwordInputLayout.setError(null));

        binding.buttonSignUp.setOnClickListener(view -> {
            binding.usernameEditText.clearFocus();
            binding.emailEditText.clearFocus();
            binding.passwordEditText.clearFocus();


            String username = binding.usernameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            dataReady = true;

            /**
             * Проверка имени пользователя
             */
            if (username.equals("")) {
                binding.usernameInputLayout.setError(getString(R.string.empty_field_error));
                dataReady = false;
            } else if (username.length() < 2) {
                binding.usernameInputLayout.setError(getString(R.string.min_length_username_field_error));
                dataReady = false;
            }

            /**
             * Проверка почты
             */
            if (email.equals("")) {
                binding.emailInputLayout.setError(getString(R.string.empty_field_error));
                dataReady = false;
            } else if (!validate(email)) {
                binding.emailInputLayout.setError(getString(R.string.invalid_email_error));
                dataReady = false;
            }

            /**
             * Проверка пароля
             */
            if (password.equals("")) {
                binding.passwordInputLayout.setError(getString(R.string.empty_field_error));
                dataReady = false;
            } else if (password.length() < 6) {
                binding.passwordInputLayout.setError(getString(R.string.min_length_password_field_error));
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

                Toast toast = Toast.makeText(getContext(),
                        getString(R.string.register_success),
                        Toast.LENGTH_LONG);
                toast.show();

            } else {
                Toast toast = Toast.makeText(getContext(),
                        getString(R.string.user_exists),
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