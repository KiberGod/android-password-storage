package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedDataViewModel;

public class ShowRecordFragment extends Fragment {

    private SharedDataViewModel viewModel;

    private static final String RECORD_ID = "record_id";

    private int record_id;

    public ShowRecordFragment() {
        // Required empty public constructor
    }

    public static ShowRecordFragment newInstance(int record_id) {
        ShowRecordFragment fragment = new ShowRecordFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_record, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);

        printRecordData(view);

        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.recordTitle, viewModel.getRecordTitleById(record_id));
        setTextViewText(view, R.id.recordCategory, viewModel.getRecordCategoryById(record_id));
        setTextViewText(view, R.id.mainRecordText, viewModel.getRecordTextById(record_id));
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        textView.setText(text);
    }
}