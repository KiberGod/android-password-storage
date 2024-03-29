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
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.components.NotFoundPage;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

import java.util.ArrayList;

public class RecordsFragment extends Fragment {
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        ((HomeActivity) requireActivity()).setTextChangedListenerToSearchBar(view, R.id.recordsScrollArea, () -> drawButtonList(view));
        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список записів
    private void drawButtonList(View view) {
        EditText searchEditText = getActivity().findViewById(R.id.searchEditText);
        ArrayList<Record> foundRecords = sharedRecordsDataViewModel.getRecords(searchEditText.getText().toString());

        if (foundRecords.size() > 0) {
            for (int i=0; i<foundRecords.size(); i++) {
                String icon_id;
                final int id = foundRecords.get(i).getId();

                if (!sharedRecordsDataViewModel.isDeletedRecordById(id)) {
                    if (sharedRecordsDataViewModel.needSetCategoryIconById(id)) {
                        icon_id = sharedCategoriesDataViewModel.getCategoryIconIdById(
                                sharedRecordsDataViewModel.getRecordCategory_idById(id)
                        );
                    } else {
                        icon_id = sharedRecordsDataViewModel.getRecordIconIdById(id);
                    }

                    ((HomeActivity) requireActivity()).drawButton(
                            view,
                            requireContext(),
                            sharedRecordsDataViewModel.getRecordTitleById(id),
                            R.id.recordsScrollArea,
                            icon_id,
                            sharedRecordsDataViewModel.getRecordAction_atById(id, sharedSettingsDataViewModel.getFiltersSortParam()),
                            ((HomeActivity) requireActivity()).getAction_atIconId(),
                            null,
                            sharedSettingsDataViewModel.getFontSizeMain(),
                            sharedSettingsDataViewModel.getFontSizeFieldCaptions(),
                            () -> ((HomeActivity) requireActivity()).setShowRecordFragment(id)
                    );
                }
            }
        } else {
            NotFoundPage.printNotFoundPage(requireContext(), view, R.id.recordsScrollArea, searchEditText.getText().toString(), "Створіть свій перший запис", sharedSettingsDataViewModel.getFontSizeMain());
        }
    }
}