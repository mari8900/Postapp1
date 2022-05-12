package com.example.postapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Bundle;
import com.example.postapp.databinding.ActivityBottomNavBinding;
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem;

public class BottomNav extends AppCompatActivity {

    private ActivityBottomNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CbnMenuItem[] menuItems = new CbnMenuItem[]{
                new CbnMenuItem(R.drawable.ic_home, R.drawable.ic_home_anim, R.id.homeFragment),
                new CbnMenuItem(R.drawable.menu_statistics, R.drawable.menu_statistics_animated, R.id.fragmentAppointment),
                new CbnMenuItem(R.drawable.ic_account, R.drawable.ic_account_anim, R.id.fragmentAccount)
        };

        binding.bottomNav.setMenuItems(menuItems, 0);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        binding.bottomNav.setupWithNavController(navController);

    }

}
