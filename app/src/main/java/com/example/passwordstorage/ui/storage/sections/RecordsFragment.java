package com.example.passwordstorage.ui.storage.sections;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.ui.HomeActivity;

public class RecordsFragment extends Fragment {

    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        sharedRecordsDataViewModel.printLogRecords();
        drawButtonList(view);

        return view;
    }

    // Функція виводить весь список записів
    private void drawButtonList(View view) {
        for (int i=0; i<sharedRecordsDataViewModel.getRecordsCount(); i++) {
            Button button = ((HomeActivity) requireActivity()).drawButton(
                    view,
                    requireContext(),
                    sharedRecordsDataViewModel.getRecordTitleByIndex(i),
                    R.id.recordsScrollArea,
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