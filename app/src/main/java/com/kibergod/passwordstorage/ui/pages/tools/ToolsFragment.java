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

        setOnClickToButton(view, R.id.passwordGeneratorButton, () -> ((HomeActivity) requireActivity()).setPasswordGeneratorFragment());
        setOnClickToButton(view, R.id.archiveButton, () -> ((HomeActivity) requireActivity()).setArchiveFragment());
        setOnClickToButton(view, R.id.rabbitSupportButton, () -> ((HomeActivity) requireActivity()).setRabbitSupportFragment());

        return view;
    }

    // Загальна функція кліку по кнопці
    private void setOnClickToButton(View view, int button_id, Runnable action) {
        Button button = view.findViewById(button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.run();
            }
        });
    }
}