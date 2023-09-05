package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.ui.HomeActivity;


public class BookmarksFragment extends Fragment {

    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список закладок
    private void drawButtonList(View view) {
        for (int i=0; i<sharedRecordsDataViewModel.getRecordsCount(); i++) {
            if (sharedRecordsDataViewModel.getBookmarkByIndex(i)) {
                Button button = ((HomeActivity) requireActivity()).drawButton(
                        view,
                        requireContext(),
                        sharedRecordsDataViewModel.getRecordTitleByIndex(i),
                        R.id.bookmarksScrollArea,
                        sharedRecordsDataViewModel.getRecordIconIdByIndex(i)
                );
                final int index = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) requireActivity()).setShowRecordFragment(index);
                    }
                });
            }
        }
    }

}