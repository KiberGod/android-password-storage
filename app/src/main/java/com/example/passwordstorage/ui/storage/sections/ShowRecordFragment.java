package com.example.passwordstorage.ui.storage.sections;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.ui.HomeActivity;

public class ShowRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String RECORD_INDEX = "record_index";

    private int recordIndex;

    ImageView bookmarkButton;

    public ShowRecordFragment() {
        // Required empty public constructor
    }

    public static ShowRecordFragment newInstance(int recordIndex) {
        ShowRecordFragment fragment = new ShowRecordFragment();
        Bundle args = new Bundle();
        args.putInt(RECORD_INDEX, recordIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordIndex = getArguments().getInt(RECORD_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        printRecordData(view);
        setOnClickToEditRecordButton(view);
        setOnClickToEditBookmarkButton(view);

        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.recordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        setTextViewText(view, R.id.recordCategory,
                sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex))
        );
        setTextViewText(view, R.id.mainRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));

        bookmarkButton = view.findViewById(R.id.bookmarkButton);
        resetBookmarkButtonColor();

        if (!sharedRecordsDataViewModel.isEmptyIconId(recordIndex))
        {
            ImageView recordIcon = view.findViewById(R.id.recordIcon);
            recordIcon.setImageResource(sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex));
        }
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        textView.setText(text);
    }

    // Функція встановлює подію переходу на сторінку редагувння запису по натисненню кнопки
    private void setOnClickToEditRecordButton(View view) {
        Button button = view.findViewById(R.id.openEditRecordPageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).setEditRecordFragment(recordIndex);
            }
        });
    }

    // Функція змінює забарвлення кнопки закладки
    private void resetBookmarkButtonColor() {
        if (sharedRecordsDataViewModel.getBookmarkByIndex(recordIndex)) {
            bookmarkButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple));
        } else {
            bookmarkButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white_2));
        }
    }

    // Функція встановлює подію зміни статусу закладки по натисненню кнопки
    private void setOnClickToEditBookmarkButton(View view) {
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedRecordsDataViewModel.editBookmarkInRecordByIndex(recordIndex);
                resetBookmarkButtonColor();
            }
        });
    }
}