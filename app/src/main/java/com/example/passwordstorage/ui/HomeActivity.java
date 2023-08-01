package com.example.passwordstorage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.passwordstorage.R;
import com.example.passwordstorage.databinding.ActivityHomeBinding;
import com.example.passwordstorage.ui.create.CreateFragment;
import com.example.passwordstorage.ui.storage.StorageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(new StorageFragment());
        setSelectedFromBottomNavBar();

        hideHintForBottomNavMenu();
    }

    // Функція встановлює прослуховування натискань на нижнє меню навігації
    private void setSelectedFromBottomNavBar() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.storage) {
                setFragment(new StorageFragment());
            } else if (item.getItemId() == R.id.create) {
                setFragment(new CreateFragment());
            }
            return true;
        });
    }

    // Перехід на інший фрагмент UI по меню навігації
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Приховання підказки для BottomNavMenu шляхом перевантаження опції довгого натискання
    private void hideHintForBottomNavMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            view.setOnLongClickListener(v -> true);
        }
    }
}