package com.kibergod.passwordstorage.ui.pages.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        ((HomeActivity) requireActivity()).setTextChangedListenerToSearchBar(view, R.id.categoriesScrollArea, () -> drawButtonList(view));
        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список категорій
    private void drawButtonList(View view) {
        EditText searchEditText = getActivity().findViewById(R.id.searchEditText);
        ArrayList<Category> foundCategories = sharedCategoriesDataViewModel.getCategories(searchEditText.getText().toString());

        if (foundCategories.size() > 0) {
            for (int i=foundCategories.size()-1; i != -1; i--) {
                final int id = foundCategories.get(i).getId();
                ((HomeActivity) requireActivity()).drawButton(
                        view,
                        requireContext(),
                        sharedCategoriesDataViewModel.getCategoryNameById(id),
                        R.id.categoriesScrollArea,
                        sharedCategoriesDataViewModel.getCategoryIconIdById(id),
                        sharedCategoriesDataViewModel.getCategoryCreated_atById(id),
                        () -> ((HomeActivity) requireActivity()).setShowCategoryFragment(id)
                );
            }
        } else {
            ((HomeActivity) requireActivity()).printNotFoundPage(view, R.id.categoriesScrollArea, searchEditText.getText().toString(), "Створіть свою першу категорію");
        }
    }
}