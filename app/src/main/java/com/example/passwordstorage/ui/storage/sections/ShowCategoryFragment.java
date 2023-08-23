package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.ui.HomeActivity;

public class ShowCategoryFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String CATEGORY_INDEX = "category_index";

    private int categoryIndex;

    public ShowCategoryFragment() {
        // Required empty public constructor
    }

    public static ShowCategoryFragment newInstance(int categoryIndex) {
        ShowCategoryFragment fragment = new ShowCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        printCategoryData(view);
        setOnClickToEditCategoryButton(view);

        return view;
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView categoryName = view.findViewById(R.id.categoryName);
        categoryName.setText(sharedCategoriesDataViewModel.getCategoryNameByIndex(categoryIndex));
        TextView categoryRecordCounter = view.findViewById(R.id.categoryRecordCounter);
        categoryRecordCounter.setText(
                "Кількість записів з даною категорією: " +
                        sharedRecordsDataViewModel.getRecordCountByCategoryId(
                                sharedCategoriesDataViewModel.getCategoryIdByIndex(categoryIndex)
                        )
        );
    }

    // Функція встановлює подію переходу на сторінку редагувння категорії по натисненню кнопки
    private void setOnClickToEditCategoryButton(View view) {
        Button button = view.findViewById(R.id.openEditCategoryPageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).setEditCategoryFragment(categoryIndex);
            }
        });
    }
}