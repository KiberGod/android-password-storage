package com.kibergod.passwordstorage.ui.pages.tools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;


public class ToolsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tools, container, false);

        setOnClickToPasswordGeneratorButton(view, R.id.passwordGeneratorButton);
        setOnClickToArchiveButton(view, R.id.archiveButton);
        return view;
    }

    // Функція встановлює подію переходу на сторінку генерації пароля по натисненню кнопки
    private void setOnClickToPasswordGeneratorButton(View view, int button_id) {
        Button button = view.findViewById(button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).setPasswordGeneratorFragment();
            }
        });
    }

    // Функція встановлює подію переходу на архіву по натисненню кнопки
    private void setOnClickToArchiveButton(View view, int button_id) {
        Button button = view.findViewById(button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).setArchiveFragment();
            }
        });
    }
}