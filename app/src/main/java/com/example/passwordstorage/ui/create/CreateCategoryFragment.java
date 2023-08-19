package com.example.passwordstorage.ui.create;

import static com.example.passwordstorage.model.Category.MAX_NAME_LENGTH;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;

public class CreateCategoryFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private CreateViewModel createViewModel;

    private TextView textViewStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        createViewModel = new ViewModelProvider(requireActivity()).get(CreateViewModel.class);

        textViewStatus = view.findViewById(R.id.createCategoryStatus);

        createViewModel.setMaxLengthForInput(view, R.id.editCreateCategoryName, MAX_NAME_LENGTH);

        setOnClickToSaveButton(view);
        return view;
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
        if (sharedCategoriesDataViewModel.checkCategoryNameUnique(categoryName)) {
            textViewStatus.setText("");
            sharedCategoriesDataViewModel.addCategory(categoryName);
            Toast.makeText(getActivity(), "Створено категорію " + categoryName, Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            textViewStatus.setText("Категорія з таким іменем вже існує");
        }
    }
}