package com.example.passwordstorage.ui.storage.sections;

import static com.example.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TITLE_LENGTH;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.ui.HomeViewModel;

import java.util.ArrayList;

public class EditRecordFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private static final String RECORD_INDEX = "record_index";

    private int recordIndex;

    private TextView textViewStatus;

    public EditRecordFragment() {
        // Required empty public constructor
    }


    public static EditRecordFragment newInstance(int recordIndex) {
        EditRecordFragment fragment = new EditRecordFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordTitle, MAX_TITLE_LENGTH);
        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.editRecordStatus);

        printRecordData(view);
        setOnClickToCancelEditRecordButton(view);
        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);

        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.editEditRecordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        setTextViewText(view, R.id.editEditRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));
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

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        String buttonText = sharedCategoriesDataViewModel.getCategoryNameById(
                sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)
        );
        if (buttonText.equals("")) {
            buttonText = homeViewModel.setEmptyCategoryText();
        }
        Button dropdownButton = view.findViewById(R.id.dropdownEditRecordCategoryButton);
        dropdownButton.setText(buttonText);

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, homeViewModel.setEmptyCategoryText()));

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(dropdownButton, adapter, categories);
            }
        });
    }

    // Функція, що спрацьовуватиме при обранні категорій з списку
    private void showDropdownMenu(Button dropdownButton, ArrayAdapter<Category> adapter, ArrayList<Category> categories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category selectedCategory = categories.get(which);
                dropdownButton.setText(selectedCategory.getName());
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        Button button = view.findViewById(R.id.saveEditRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditRecord(view);
            }
        });
    }

    // Обробка редагування запису
    private void getEditRecord(View view) {
        EditText textInput = view.findViewById(R.id.editEditRecordText);
        EditText titleInput = view.findViewById(R.id.editEditRecordTitle);
        String recordTitle = titleInput.getText().toString();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle, recordTitle)) {
                textViewStatus.setText("");
                Button categoryButton = view.findViewById(R.id.dropdownEditRecordCategoryButton);
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(categoryButton.getText().toString());
                sharedRecordsDataViewModel.editRecord(recordIndex, recordTitle, textInput.getText().toString(), category_id);
                Toast.makeText(getActivity(), "Запис змінено " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з таким заголовком вже існує");
            }
        } else {
            textViewStatus.setText("Заголовок запису не може бути порожнім");
        }
    }
}