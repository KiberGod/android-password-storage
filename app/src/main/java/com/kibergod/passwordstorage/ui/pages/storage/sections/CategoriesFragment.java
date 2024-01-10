package com.kibergod.passwordstorage.ui.pages.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.components.NotFoundPage;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        ((HomeActivity) requireActivity()).setTextChangedListenerToSearchBar(view, R.id.categoriesScrollArea, () -> drawButtonList(view));
        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список категорій
    private void drawButtonList(View view) {
        EditText searchEditText = getActivity().findViewById(R.id.searchEditText);
        ArrayList<Category> foundCategories = sharedCategoriesDataViewModel.getCategories(searchEditText.getText().toString());

        if (foundCategories.size() > 0) {
            for (int i=0; i<foundCategories.size(); i++) {
                final int id = foundCategories.get(i).getId();
                ((HomeActivity) requireActivity()).drawButton(
                        view,
                        requireContext(),
                        sharedCategoriesDataViewModel.getCategoryNameById(id),
                        R.id.categoriesScrollArea,
                        sharedCategoriesDataViewModel.getCategoryIconIdById(id),
                        sharedCategoriesDataViewModel.getCategoryAction_atById(id, sharedSettingsDataViewModel.getFiltersSortParam()),
                        ((HomeActivity) requireActivity()).getAction_atIconId(),
                        null,
                        () -> ((HomeActivity) requireActivity()).setShowCategoryFragment(id)
                );
            }
        } else {
            NotFoundPage.printNotFoundPage(requireContext(), view, R.id.categoriesScrollArea, searchEditText.getText().toString(), "Створіть свою першу категорію");
        }
    }
}