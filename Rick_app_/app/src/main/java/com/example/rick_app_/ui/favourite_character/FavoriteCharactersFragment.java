package com.example.rick_app_.ui.favourite_character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.AppDatabase;
import com.example.data.FavoriteCharacterDao;
import com.example.data.FavoriteCharacterRepositoryImpl;
import com.example.data.model.FavoriteCharacterEntity;
import com.example.domain.model.Character;
import com.example.domain.repository.FavoriteCharacterRepository;
import com.example.rick_app_.R;
import com.example.rick_app_.ui.character.CharacterAdapter;
import com.example.rick_app_.ui.character.CharactersViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCharactersFragment extends Fragment {
    private RecyclerView favoriteCharacterRecyclerView;
    private CharacterAdapter characterAdapter;
    private FavoriteCharacterDao favoriteCharacterDao;
    private FavoriteCharacterRepository favoriteCharacterRepository;
    private CharactersViewModel characterViewModel;

    private ImageButton menuButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_characters, container, false);

        favoriteCharacterRecyclerView = view.findViewById(R.id.favoriteCharacterRecyclerView);
        favoriteCharacterRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        favoriteCharacterDao = AppDatabase.getInstance(requireContext()).favoriteCharacterDao();
        favoriteCharacterRepository = new FavoriteCharacterRepositoryImpl(requireContext());

        // Инициализация ViewModel
        characterViewModel = new ViewModelProvider(this,
                new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new CharactersViewModel(favoriteCharacterRepository);
                    }
                }).get(CharactersViewModel.class);

        loadFavoriteCharacters(view);

        return view;
    }

    private void loadFavoriteCharacters(View view) {
        new Thread(() -> {
            List<FavoriteCharacterEntity> favoriteCharacters = favoriteCharacterDao.getAllFavoriteCharacters();
            requireActivity().runOnUiThread(() -> {
                List<Character> characters = convertToCharacters(favoriteCharacters); // Используем правильный тип
                characterAdapter = new CharacterAdapter(characters, characterViewModel); // Передаем ViewModel
                favoriteCharacterRecyclerView.setAdapter(characterAdapter);
            });
        }).start();
    }

    private List<Character> convertToCharacters(List<FavoriteCharacterEntity> favoriteCharacterEntities) {
        List<Character> characters = new ArrayList<>();
        for (FavoriteCharacterEntity entity : favoriteCharacterEntities) {
            Character character = new Character(
                    entity.getId(),
                    entity.getName(),
                    entity.getSpecies(),
                    entity.getStatus(),
                    entity.getImageUrl(),
                    entity.getGender()
            );
            characters.add(character);
        }
        return characters;
    }
}
