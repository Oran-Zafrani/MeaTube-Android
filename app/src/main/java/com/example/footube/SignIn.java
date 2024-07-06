package com.example.footube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SignIn extends AppCompatActivity {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_DARK_MODE = "dark_mode";
    UserManager userManager = UserManager.getInstance(); // Get the singleton instance
    TextView linkToSignUp;
    TextView linkToSigninguest;
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

        //Sign in as a guess
        Intent Signinguest = new Intent(this, MoviesList.class);
        linkToSigninguest = findViewById(R.id.Signinguest);
        linkToSigninguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Signinguest);
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
                closeKeyboard(v);
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (userManager.correctSignIn(user, pass)) {
                    User userInstance = userManager.getUser(user);
                    MoviesListIntent.putExtra("user", userInstance);

                    username.setText("");
                    password.setText("");
                    errorTextView.setVisibility(View.GONE);
                    username.requestFocus();
                    username.setSelection(username.getText().length());

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

        //see the password
//        EditText passwordEditText = findViewById(R.id.tvPassword);
//
//        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
//            boolean isPasswordVisible = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
////                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
////                        if (isPasswordVisible) {
////                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
////                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeopen, 0);
////                        } else {
////                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
////                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeclose, 0);
////                        }
////                        isPasswordVisible = !isPasswordVisible;
////                        // Move the cursor to the end of the text
////                        passwordEditText.setSelection(passwordEditText.getText().length());
////                        return true;
////                    }
////                }
//                passwordEditText.setHint();
//                return false;
//            }
//        });

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

    public void closeKeyboard(View view) {
        // Check if no view has focus
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        } else {
            Toast.makeText(this, "No view has focus", Toast.LENGTH_SHORT).show();
        }
    }
}
