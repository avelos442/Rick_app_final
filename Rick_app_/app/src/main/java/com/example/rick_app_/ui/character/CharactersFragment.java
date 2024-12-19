package com.example.rick_app_.ui.character;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.CharacterRepositoryImpl;
import com.example.data.FavoriteCharacterRepositoryImpl;
import com.example.domain.model.Character;
import com.example.data.model.FavoriteCharacterEntity;
import com.example.domain.repository.CharacterRepository;
import com.example.domain.repository.FavoriteCharacterRepository;
import com.example.rick_app_.R;

import java.util.ArrayList;
import java.util.List;

public class CharactersFragment extends Fragment {
    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private CharacterRepository characterRepository;
    private FavoriteCharacterRepository favoriteCharacterRepository;
    private CharactersViewModel characterViewModel;
    private ImageButton menuButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characters, container, false);

        // Инициализация репозиториев
        favoriteCharacterRepository = new FavoriteCharacterRepositoryImpl(requireContext());
        characterRepository = new CharacterRepositoryImpl(requireContext(), favoriteCharacterRepository);

        // Инициализация ViewModel
        characterViewModel = new ViewModelProvider(this, new CharacterViewModelFactory(favoriteCharacterRepository))
                .get(CharactersViewModel.class);

        // Инициализация RecyclerView и Adapter
        recyclerView = view.findViewById(R.id.characterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        characterAdapter = new CharacterAdapter(new ArrayList<>(), characterViewModel);
        recyclerView.setAdapter(characterAdapter);

        // Загрузка персонажей через ViewModel
        loadCharacters();

        return view;
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

    private void loadCharacters() {
        characterRepository.getCharactersAsync(new CharacterRepository.CharacterCallback() {
            @Override
            public void onSuccess(List<Character> characters) {
                if (characters != null) {
                    List<FavoriteCharacterEntity> favoriteCharacterEntities = convertToFavoriteCharacterEntities(characters);
                    List<Character> characterList = convertToCharacters(favoriteCharacterEntities);
                    characterAdapter.updateData(characterList);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("CharactersFragment", "Failed to load characters: " + t.getMessage());
                Toast.makeText(requireContext(), "Error loading characters", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<FavoriteCharacterEntity> convertToFavoriteCharacterEntities(List<Character> characters) {
        List<FavoriteCharacterEntity> favoriteCharacters = new ArrayList<>();
        for (Character character : characters) {
            favoriteCharacters.add(new FavoriteCharacterEntity(
                    character.getId(),
                    character.getName(),
                    character.getStatus(),
                    character.getSpecies(),
                    character.getImage(),
                    character.getGender()
            ));
        }
        return favoriteCharacters;
    }
}
