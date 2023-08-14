package com.example.passwordstorage.ui.create;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedDataViewModel;
import com.example.passwordstorage.model.Category;


public class CreateRecordFragment extends Fragment {

    private SharedDataViewModel sharedDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);

        setDropdownMenu(view);

        return view;
    }


    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setDropdownMenu(View view) {
        Button dropdownButton = view.findViewById(R.id.dropdownButton);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sharedDataViewModel.getAllCategories());
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(dropdownButton, adapter);
            }
        });

    }

    // Функція, що спрацьовуватиме при обранні категорій з списку
    private void showDropdownMenu(Button dropdownButton, ArrayAdapter<Category> adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category selectedCategory = sharedDataViewModel.getAllCategories().get(which);
                dropdownButton.setText(selectedCategory.getName());
                dialog.dismiss();
            }
        });
        builder.show();
    }
}