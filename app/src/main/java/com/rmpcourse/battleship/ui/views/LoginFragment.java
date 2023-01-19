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
import com.rmpcourse.battleship.databinding.FragmentLoginBinding;
import com.rmpcourse.battleship.ui.viewmodel.PlayerViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private PlayerViewModel mPlayerViewModel;

    private boolean dataReady = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Создаем объект представления
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Получаем экземпляр модели данных
        mPlayerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        // Вешаем слушатели на поля ввода
        binding.usernameEditText.setOnFocusChangeListener((view, b) -> binding.usernameInputLayout.setError(null));
        binding.passwordEditText.setOnFocusChangeListener((view, b) -> binding.passwordInputLayout.setError(null));

        // Кнопка "Войти"
        binding.buttonSignIn.setOnClickListener(view -> {
            binding.usernameInputLayout.clearFocus();
            binding.passwordInputLayout.clearFocus();

            dataReady = true;
            String username = binding.usernameEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

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
             * Проверка пароля
             */
            if (password.equals("")) {
                binding.passwordInputLayout.setError(getString(R.string.empty_field_error));
                dataReady = false;
            } else if (password.length() < 6) {
                binding.passwordInputLayout.setError(getString(R.string.min_length_password_field_error));
                dataReady = false;
            }

            // Если не все поля заполнены - не продолжаем
            if (!dataReady) return;

            // Если пользователь сущестувет
            if (isPlayerExists(username)) {
                // Если пароль верный
                if (isPasswordMatching(password)) {
                    // переходим на другую страницу с помощью навигации Navigation Component и передаем нужные параметры на фрамент
                    NavDirections action = LoginFragmentDirections
                            .actionLoginFragmentToStartFragment(mPlayerViewModel.getPlayer().playerId);
                    Navigation.findNavController(view).navigate(action);
                } else {
                    // Выводим всплывающее сообщение
                    Toast toast = Toast.makeText(getContext(),
                            getString(R.string.incorrect_password),
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                // Выводим всплывающее сообщение
                Toast toast = Toast.makeText(getContext(),
                        getString(R.string.user_not_registered),
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Устанавливаем представление для фрагмента
        return binding.getRoot();
    }

    // Проверка существует ли такой пользователь в БД
    private boolean isPlayerExists(String username) {
        return mPlayerViewModel.findPlayerByUsername(username);
    }

    // Проверка правильный ли пароль
    private boolean isPasswordMatching(String password) {
        return Objects.equals(mPlayerViewModel.getPlayer().password, password);
    }
}