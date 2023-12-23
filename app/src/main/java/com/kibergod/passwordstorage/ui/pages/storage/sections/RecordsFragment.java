package com.kibergod.passwordstorage.ui.pages.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

public class RecordsFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список записів
    private void drawButtonList(View view) {
        for (int i=sharedRecordsDataViewModel.getRecordsCount()-1; i != -1; i--) {
            String icon_id;
            if (sharedRecordsDataViewModel.needSetCategoryIconByIndex(i)) {
                icon_id = sharedCategoriesDataViewModel.getCategoryIconIdById(
                        sharedRecordsDataViewModel.getRecordCategory_idByIndex(i)
                );
            } else {
                icon_id = sharedRecordsDataViewModel.getRecordIconIdByIndex(i);
            }

            final int index = i;
            ((HomeActivity) requireActivity()).drawButton(
                    view,
                    requireContext(),
                    sharedRecordsDataViewModel.getRecordTitleByIndex(i),
                    R.id.recordsScrollArea,
                    icon_id,
                    sharedRecordsDataViewModel.getRecordCreated_atByIndex(i),
                    () -> ((HomeActivity) requireActivity()).setShowRecordFragment(index)
            );
        }
    }
}