package com.example.passwordstorage.ui.storage.sections;

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
import com.example.passwordstorage.ui.HomeViewModel;

public class EditCategoryFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

    private static final String CATEGORY_INDEX = "category_index";

    private int categoryIndex;

    private TextView textViewStatus;

    public EditCategoryFragment() {
        // Required empty public constructor
    }

    public static EditCategoryFragment newInstance(int categoryIndex) {
        EditCategoryFragment fragment = new EditCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_INDEX, categoryIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryIndex = getArguments().getInt(CATEGORY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        textViewStatus = view.findViewById(R.id.editCategoryStatus);

        homeViewModel.setMaxLengthForInput(view, R.id.editEditCategoryName, MAX_NAME_LENGTH);

        printCategoryData(view);
        setOnClickToCancelEditCategoryButton(view);
        setOnClickToSaveButton(view);

        return view;
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView categoryName = view.findViewById(R.id.editEditCategoryName);
        categoryName.setText(sharedCategoriesDataViewModel.getCategoryNameByIndex(categoryIndex));
    }

    // Функція встановлює подію переходу на попередню сторінку (з переглядом категорії)
    private void setOnClickToCancelEditCategoryButton(View view) {
        Button button = view.findViewById(R.id.cancelEditCategoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        Button button = view.findViewById(R.id.saveEditCategoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditCategory(view);
            }
        });
    }

    // Обробка редагування категорії
    private void getEditCategory(View view) {
        EditText nameInput = view.findViewById(R.id.editEditCategoryName);
        String categoryName = nameInput.getText().toString();
        if (categoryName.length() != 0) {
            if (sharedCategoriesDataViewModel.checkCategoryNameUnique(categoryName)) {
                textViewStatus.setText("");
                sharedCategoriesDataViewModel.editCategory(categoryIndex, categoryName);
                Toast.makeText(getActivity(), "Змінено категорію " + categoryName, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Категорія з таким іменем вже існує");
            }
        } else {
            textViewStatus.setText("І`мя не може бути порожнім");
        }
    }
}