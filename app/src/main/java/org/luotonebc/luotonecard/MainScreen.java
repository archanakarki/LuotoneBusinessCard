package org.luotonebc.luotonecard;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {
    private Button myProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myProfile = findViewById(R.id.profileButton);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileScreen();
            }
        });
    }

    public void openProfileScreen() {
        Intent intent = new Intent (this, ProfileScreen.class);
        startActivity(intent);
    }

}
