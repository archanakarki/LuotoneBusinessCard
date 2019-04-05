package org.luotonebc.luotonecard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileScreen extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    private EditText emailText;
    private EditText phoneText;
    private EditText companyText;
    private ImageView imageView;
    private TextInputEditText textInputEditEmail;
    private TextInputEditText textInputEditPhone;
    private ImageButton imageButton_back;
    public static final String emailText_value = "email";
    private static final String phoneText_value = "phone";
    private static final String companyText_value = "company";

    private String textToEmail;
    private String textToPhone;
    private String textTocompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        emailText = (EditText) findViewById(R.id.EmailID);
        phoneText = (EditText) findViewById(R.id.phoneID);
        companyText = (EditText) findViewById(R.id.companyID);
        textInputEditEmail = findViewById(R.id.EmailID);
        textInputEditPhone = findViewById(R.id.phoneID);
        imageButton_back = findViewById(R.id.searchImageButton);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this,MainScreen.class);
                startActivity(intent);
            }
        });

        ImageButton imageButton = findViewById(R.id.cameraButton);
        imageView = findViewById(R.id.profileImageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        Boolean phoneValue = phoneNumberValidation(phoneText.getText().toString());
                        Boolean value = validateEmail();
                        if(value == true && phoneValue == true){
                            save_values();
                            Toast.makeText(ProfileScreen.this, "Profile information saved", Toast.LENGTH_SHORT).show();
                        }
                        else if(phoneValue==false){
                            textInputEditPhone.setError("Invalid phone number");
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }

            private void save_values() {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(emailText_value, emailText.getText().toString());
                editor.putString(phoneText_value, phoneText.getText().toString());
                editor.putString(companyText_value, companyText.getText().toString());
                editor.apply();
            }
        });
        load_values();
        update();
    }
    private void update() {
        emailText.setText(textToEmail);
        phoneText.setText(textToPhone);
        companyText.setText(textTocompany);
    }

    private void load_values() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        textToEmail = sharedPreferences.getString(emailText_value, "");
        textToPhone = sharedPreferences.getString(phoneText_value, "");
        textTocompany = sharedPreferences.getString(companyText_value, "");
    }

    private boolean validateEmail(){
        String emailInput = textInputEditEmail.getText().toString().trim();
        if(emailInput.isEmpty()){
            textInputEditEmail.setError("Field can't be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputEditEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEditEmail.setError(null);
            return true;
        }
    }
    public boolean phoneNumberValidation(String phoneNumber)
    {
        // Validation for international phone numbers per E.164 (Max. length with country calling code is 15)
        // Leading '+' allowed, otherwise digits only

        String expression = "^\\+?\\d{7,15}$";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(expression);
        java.util.regex.Matcher matcher = pattern.matcher(phoneNumber);

        if(matcher.matches())
        {
            return true; // Valid phone number
        }
        else
        {
            return false; // Not a valid phone number
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.profileToolbaricon:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }
}
