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
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

import java.util.ArrayList;


public class BookmarksFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        ((HomeActivity) requireActivity()).setTextChangedListenerToSearchBar(view, R.id.bookmarksScrollArea, () -> drawButtonList(view));
        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список закладок
    private void drawButtonList(View view) {
        EditText searchEditText = getActivity().findViewById(R.id.searchEditText);
        ArrayList<Record> foundRecords = sharedRecordsDataViewModel.getRecords(searchEditText.getText().toString());

        for (int i=foundRecords.size()-1; i != -1; i--) {
            final int id = foundRecords.get(i).getId();
            if (sharedRecordsDataViewModel.getBookmarkById(id)) {
                String icon_id;
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
                        R.id.bookmarksScrollArea,
                        icon_id,
                        sharedRecordsDataViewModel.getRecordCreated_atById(id),
                        () -> ((HomeActivity) requireActivity()).setShowRecordFragment(id)
                );
            }
        }
    }
}