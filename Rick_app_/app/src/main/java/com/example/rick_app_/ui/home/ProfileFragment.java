package com.example.rick_app_.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.data.FavoriteCharacterRepositoryImpl;
import com.example.data.UserRepository;
import com.example.domain.repository.FavoriteCharacterRepository;
import com.example.domain.repository.UserRepositoryInterface;
import com.example.rick_app_.R;

public class ProfileFragment extends Fragment {

    private TextView emailTextView;
    private TextView usernameTextView;
    private TextView favoriteCountTextView; // TextView для отображения количества избранных персонажей
    private ImageButton menuButton;
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d("ProfileFragment", "ProfileFragment Created");

        // Инициализация репозиториев
        UserRepositoryInterface userRepository = new UserRepository(requireContext());
        FavoriteCharacterRepository favoriteCharacterRepository = new FavoriteCharacterRepositoryImpl(requireContext());

        // Инициализация ViewModel
        profileViewModel = new ProfileViewModel(userRepository, favoriteCharacterRepository);

        // Отображаем email и логин на странице профиля
        emailTextView = view.findViewById(R.id.email_text);
        usernameTextView = view.findViewById(R.id.username_text);
        favoriteCountTextView = view.findViewById(R.id.favorite_characters_text);

        // Наблюдаем за изменениями данных пользователя
        profileViewModel.getUserEmail().observe(getViewLifecycleOwner(), email -> {
            emailTextView.setText("Почта: " + email);
        });

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            usernameTextView.setText("Джерри из вселенной: " + username);
        });

        profileViewModel.getFavoriteCount().observe(getViewLifecycleOwner(), count ->
                favoriteCountTextView.setText("Избранные персонажи: " + count));

        return view;
    }
}
