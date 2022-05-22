package com.example.diarynotesapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.diarynotesapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        /*AlertDialog.Builder builder;
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            builder = new AlertDialog.Builder(this);
        }
        else
        {
            builder = new AlertDialog.Builder(this, animazione);
        }
        builder.setView(view).create();
        TextView text=(TextView) findViewById(R.id.infoView1);
        builder.setCancelable(false);
        builder.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                tips = 0;
                dialog.cancel();
            }
        });
        builder.show();*/

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,  R.id.navigation_tasks, R.id.navigation_notes, R.id.navigation_alerts)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}