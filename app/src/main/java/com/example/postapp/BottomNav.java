package com.example.postapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Bundle;
import com.example.postapp.databinding.ActivityBottomNavBinding;
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem;

public class BottomNav extends AppCompatActivity {

    private ActivityBottomNavBinding binding;
    //MeowBottomNavigation bottomNavigation;

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

//        bottomNavigation = findViewById(R.id.meow_bottom_nav);
//
//        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

// as soon as the application opens the first
// fragment should be shown to the user
// in this case it is algorithm fragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

//        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
//        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_date));
//        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_account));

//        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(MeowBottomNavigation.Model item) {
//                Fragment fragment = null;
//
//                switch(item.getId()) {
//                    case 1:
//                        fragment = new HomeFragment();
//                        break;
//                    case 2:
//                        fragment = new CreateAppointmentFragment();
//                        break;
//                    case 3:
//                        fragment = new MyAccountFragment();
//                        break;
//                }
//                loadFragment(fragment);
//            }
//        });
//
//
//        bottomNavigation.show(1, true);
//        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
//
//            }
//        });

//    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            // By using switch we can easily get
//            // the selected fragment
//            // by using there id.
//            Fragment selectedFragment = null;
//            switch (item.getItemId()) {
//                case R.id.home:
//                    selectedFragment = new HomeFragment();
//                    break;
//                case R.id.createAppt:
//                    selectedFragment = new CreateAppointmentFragment();
//                    break;
//                case R.id.profile:
//                    selectedFragment = new MyAccountFragment();
//                    break;
//            }
//            // It will help to replace the
//            // one fragment to other.
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frameLayout, selectedFragment)
//                    .commit();
//            return true;
//        }
//    };

//    private void loadFragment(Fragment fragment) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
//    }