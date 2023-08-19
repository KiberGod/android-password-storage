package com.example.passwordstorage.ui.storage.sections;

import static com.example.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TITLE_LENGTH;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.ui.HomeViewModel;

public class EditRecordFragment extends Fragment {

    private HomeViewModel homeViewModel;

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

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordTitle, MAX_TITLE_LENGTH);
        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordText, MAX_TEXT_LENGTH);

        printRecordData(view);
        setOnClickToCancelEditRecordButton(view);

        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.editEditRecordTitle, sharedRecordsDataViewModel.getRecordTitleById(record_id));
        setTextViewText(view, R.id.editEditRecordText, sharedRecordsDataViewModel.getRecordTextById(record_id));
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        EditText editText = view.findViewById(textViewId);
        editText.setText(text);
    }

    // Функція встановлює подію переходу на попередню сторінку (з переглядом запису)
    private void setOnClickToCancelEditRecordButton(View view) {
        Button button = view.findViewById(R.id.cancelEditRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}