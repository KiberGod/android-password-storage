package com.example.passwordstorage.ui.create;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.passwordstorage.R;
import com.example.passwordstorage.ui.HomeActivity;

public class CreateFragment extends Fragment {

    private CreateViewModel mViewModel;

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create, container, false);

        setOnClickToCreateButton(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Функція встановлює подію переходу на сторінку створення запису по натисненню кнопки
    private void setOnClickToCreateButton(View view) {
        Button createRecordButton = view.findViewById(R.id.createRecordButton);
        createRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).setCreateRecordFragment();
            }
        });
    }
}