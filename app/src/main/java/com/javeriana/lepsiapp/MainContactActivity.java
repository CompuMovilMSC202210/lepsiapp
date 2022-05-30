package com.javeriana.lepsiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.firebase.auth.FirebaseAuth;

import com.javeriana.lepsiapp.databinding.ActivityContactMainBinding;
import com.javeriana.lepsiapp.ui.login.LoginActivity;


public class MainContactActivity extends AppCompatActivity {

    private ActivityContactMainBinding binding1;
    private FirebaseAuth mAuth;
    Toolbar toolbarL1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding1 = ActivityContactMainBinding.inflate(getLayoutInflater());
        setContentView(binding1.getRoot());

        toolbarL1 = findViewById(R.id.barratool1);
        setSupportActionBar(toolbarL1);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home1, R.id.navigation_chat1)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_contact_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding1.navView1, navController);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case (R.id.LoGout):
                logOut();
                return true;
            case (R.id.Help):
                help();
                return true;
            case (R.id.Profile):
                //profile();
                return true;
        }
        return false;
    }




    public void logOut() {
        //cierre sesion
        mAuth.signOut();
        //UpdateUi
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void help() {
        Intent intent = new Intent(this, HelpInformation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
