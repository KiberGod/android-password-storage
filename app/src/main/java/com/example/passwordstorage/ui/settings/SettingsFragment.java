package com.example.passwordstorage.ui.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedSettingsDataViewModel;
import com.example.passwordstorage.ui.HomeActivity;

public class SettingsFragment extends Fragment {

    private SharedSettingsDataViewModel sharedSettingsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);

        printSettingsData(view);
        setOnClickToSwitch(view, R.id.activityProtectionFlag, () -> sharedSettingsDataViewModel.editActivityProtection());
        setOnClickToSwitch(view, R.id.inputPassClearingFlag, () -> sharedSettingsDataViewModel.editInputPassClearing());
        setOnClickDefaultSettingsButton(view);

        return view;
    }

    // Функція виводить дані налаштуваннь на екран
    private void printSettingsData(View view) {
        Switch activityProtectionSwitch = view.findViewById(R.id.activityProtectionFlag);
        activityProtectionSwitch.setChecked(sharedSettingsDataViewModel.getActivityProtection());

        Switch inputPassClearingSwitch = view.findViewById(R.id.inputPassClearingFlag);
        inputPassClearingSwitch.setChecked(sharedSettingsDataViewModel.getInputPassClearing());
    }

    // Встановлює обробник натискань на перемикач налаштування ActivityProtection
    private void setOnClickToSwitch(View view, int switch_id, Runnable onClickRunnable) {
        Switch settingSwitch = view.findViewById(switch_id);

        settingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRunnable.run();
            }
        });
    }

    // Встановлює обробник натискання на скид налаштуваннь до стандартних
    private void setOnClickDefaultSettingsButton(View view) {
        Button button = view.findViewById(R.id.setDefaultSettingsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedSettingsDataViewModel.setDefaultSettings();
                printSettingsData(view);
            }
        });
    }
}