package com.example.beautyproduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.homebottomnavigationviews);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.MenuHomebottomNavigationHome);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //onCreateOtionMenu function is used to created menu
        MenuInflater menuInflater = getMenuInflater();//MenuInflater class is used to store menu
        menuInflater.inflate(R.menu.home_menu_items, menu);//inflate method is used to store and call menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.HomeMenuAboutUs) {

        } else if (item.getItemId() == R.id.HomeMenuSettings) {

        } else if (item.getItemId() == R.id.HomeMenuLogout) {

        } else if (item.getItemId() == R.id.HomeMenuMyProfile) {
            Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }

    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    SearchFragment searchFragment = new SearchFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.MenuHomebottomNavigationHome) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, homeFragment).commit();
        } else if (item.getItemId() == R.id.MenuHomebottomNavigationCategory) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, categoryFragment).commit();
        } else if (item.getItemId() == R.id.MenuHomebottomNavigationSearch) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, searchFragment).commit();
        } else if (item.getItemId() == R.id.MenuHomebottomNavigationProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout, profileFragment).commit();
        }
        return true;
    }
}