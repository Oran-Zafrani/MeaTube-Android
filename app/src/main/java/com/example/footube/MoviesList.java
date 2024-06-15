package com.example.footube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MoviesList extends AppCompatActivity {
    TextView login;
    private DrawerLayout drawerLayout;
    private Button sideMenuButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuButton = findViewById(R.id.side_menu_button);

        // Set OnClickListener for side menu button
        sideMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when button is clicked
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Intent signInIntent = new Intent(this, SignIn.class);
        login = findViewById(R.id.login_link);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signInIntent);
            }
        });
    }
}