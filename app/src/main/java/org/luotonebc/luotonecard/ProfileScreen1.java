package org.luotonebc.luotonecard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfileScreen1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_back_button); //Display's Back button handler
        getSupportActionBar().setDisplayShowTitleEnabled(false); //No display of title text in toolbar


    }

    //Menu item added in toolbar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater profilePageMenu = getMenuInflater();
        profilePageMenu.inflate(R.menu.profile_menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.backButton:
                Intent backToMainScreen = new Intent (this, MainScreen.class);
                startActivity(backToMainScreen);
                Toast.makeText(this, "Back Button is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profileMenu:
                Intent toSettingScreen = new Intent (this, LoginScreen.class);
                startActivity(toSettingScreen);
                Toast.makeText(this, "Setting Menu is Selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
