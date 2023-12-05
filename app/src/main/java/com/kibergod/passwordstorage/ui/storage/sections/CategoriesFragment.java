package com.kibergod.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.ui.HomeActivity;

public class CategoriesFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);

        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список категорій
    private void drawButtonList(View view) {
        for (int i=0; i<sharedCategoriesDataViewModel.getCategoriesCount(); i++) {
            Button button = ((HomeActivity) requireActivity()).drawButton(
                    view,
                    requireContext(),
                    sharedCategoriesDataViewModel.getCategoryNameByIndex(i),
                    R.id.categoriesScrollArea,
                    sharedCategoriesDataViewModel.getCategoryIconIdByIndex(i)
            );
            final int index = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) requireActivity()).setShowCategoryFragment(index);
                }
            });
        }
    }
}