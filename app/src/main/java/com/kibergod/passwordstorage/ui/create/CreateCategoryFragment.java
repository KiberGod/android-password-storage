package com.kibergod.passwordstorage.ui.create;

import static com.kibergod.passwordstorage.model.Category.MAX_NAME_LENGTH;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.ui.HomeActivity;
import com.kibergod.passwordstorage.ui.HomeViewModel;

public class CreateCategoryFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private HomeViewModel homeViewModel;

    private TextView textViewStatus;

    private int tempIconId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        textViewStatus = view.findViewById(R.id.createCategoryStatus);

        homeViewModel.setMaxLengthForInput(view, R.id.editCreateCategoryName, MAX_NAME_LENGTH);

        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
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
        if (categoryName.length() != 0) {
            if (sharedCategoriesDataViewModel.checkCategoryNameUnique(categoryName)) {
                textViewStatus.setText("");
                sharedCategoriesDataViewModel.addCategory(categoryName, tempIconId);
                Toast.makeText(getActivity(), "Створено категорію " + categoryName, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Категорія з таким іменем вже існує");
            }
        } else {
            textViewStatus.setText("І`мя не може бути порожнім");
        }
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.createCategoryIcon);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).showIconSelectionDialog(requireContext(), iconResourceId -> {
                    tempIconId = iconResourceId;
                    imageView.setImageResource(iconResourceId);
                });
            }
        });
    }
}