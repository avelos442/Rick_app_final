package com.example.rick_app_;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EnterActivity extends AppCompatActivity {

    private EnterViewModel enterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        Button enterButton = findViewById(R.id.enter_button);
        Button recipeButton = findViewById(R.id.escape_button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        enterViewModel = new EnterViewModel(sharedPreferences);

        // Наблюдение за состоянием авторизации
        enterViewModel.getIsLoggedIn().observe(this, isLoggedIn -> {
            recipeButton.setEnabled(isLoggedIn);
        });

        enterButton.setOnClickListener(v -> {
            Intent intent = new Intent(EnterActivity.this, AuthActivity.class);
            startActivity(intent);
        });

        recipeButton.setOnClickListener(v -> {
            // Переход на экран рецептов, если пользователь авторизован
            if (enterViewModel.getIsLoggedIn().getValue() != null && enterViewModel.getIsLoggedIn().getValue()) {
//                Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
//                startActivity(intent);
            } else {
                // Если пользователь не авторизован, перенаправляем его на экран входа
                Intent intent = new Intent(EnterActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });
    }
}