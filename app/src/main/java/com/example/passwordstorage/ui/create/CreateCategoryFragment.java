package com.example.passwordstorage.ui.create;

import static com.example.passwordstorage.model.Category.MAX_NAME_LENGTH;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedDataViewModel;

public class CreateCategoryFragment extends Fragment {

    private SharedDataViewModel sharedDataViewModel;

    private TextView textViewStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);

        textViewStatus = view.findViewById(R.id.createCategoryStatus);

        setMaxLengthForInputName(view);
        setOnClickToSaveButton(view);
        return view;
    }

    // Функція встановлює обмеження у кількості символів для назви категорії
    private void setMaxLengthForInputName(View view) {
        EditText editText = view.findViewById(R.id.editCreateCategoryName);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(MAX_NAME_LENGTH);
        editText.setFilters(filters);
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        Button button = view.findViewById(R.id.saveNewCategoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewCategory(view);
            }
        });
    }

    // Обробка створення нової категорії
    private void getNewCategory(View view) {
        EditText editText = view.findViewById(R.id.editCreateCategoryName);
        String categoryName = editText.getText().toString();
        if (sharedDataViewModel.checkNameUnique(categoryName)) {
            textViewStatus.setText("");
            sharedDataViewModel.addCategory(categoryName);
            Toast.makeText(getActivity(), "Створено категорію " + categoryName, Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            textViewStatus.setText("Категорія з таким іменем вже існує");
        }
    }
}