package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;

public class EditRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private static final String RECORD_ID = "record_id";

    private int record_id;

    public EditRecordFragment() {
        // Required empty public constructor
    }


    public static EditRecordFragment newInstance(int record_id) {
        EditRecordFragment fragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putInt(RECORD_ID, record_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            record_id = getArguments().getInt(RECORD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_record, container, false);

        return view;
    }
}