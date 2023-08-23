package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;

public class ShowCategoryFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

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
        printCategoryData(view);

        return view;
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView textView = view.findViewById(R.id.categoryName);
        textView.setText(sharedCategoriesDataViewModel.getCategoryNameByIndex(categoryIndex));
    }
}