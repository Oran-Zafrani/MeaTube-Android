package com.example.footube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SignIn extends AppCompatActivity {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_DARK_MODE = "dark_mode";
    UserManager userManager = UserManager.getInstance(); // Get the singleton instance
    TextView linkToSignUp;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Intent SignUpIntent = new Intent(this, SignUp.class);
        linkToSignUp = findViewById(R.id.tvSignUpLink);

        linkToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignUpIntent);
            }
        });

        Intent MoviesListIntent = new Intent(this, MoviesList.class);
        login = findViewById(R.id.btnLogin);
        EditText username = findViewById(R.id.tvUsername);
        EditText password = findViewById(R.id.tvPassword);
        TextView errorTextView = findViewById(R.id.errorTextView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (userManager.correctSignIn(user, pass)) {
                    User userInstance = userManager.getUser(user);
                    MoviesListIntent.putExtra("user", userInstance);
                    startActivity(MoviesListIntent);
                } else {
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Dark mode
        ImageButton themeToggleButton = findViewById(R.id.btnDarkMode);
        themeToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme();
            }
        });
    }

    private void toggleTheme() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, false);

        // Toggle theme
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            preferences.edit().putBoolean(PREF_DARK_MODE, false).apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            preferences.edit().putBoolean(PREF_DARK_MODE, true).apply();
        }

        // Recreate activity to apply the new theme
        recreate();
    }
}
