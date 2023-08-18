package com.example.passwordstorage.ui.create;

import static com.example.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TITLE_LENGTH;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedDataViewModel;
import com.example.passwordstorage.model.Category;

import java.util.ArrayList;


public class CreateRecordFragment extends Fragment {

    private SharedDataViewModel sharedDataViewModel;
    private CreateViewModel createViewModel;

    private TextView textViewStatus;

    private final String DEFAULT_CATEGORY_TEXT = "Відсутня";

    // id обраної категорії
    private Integer selectedCategoryId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        sharedDataViewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
        createViewModel = new ViewModelProvider(requireActivity()).get(CreateViewModel.class);

        createViewModel.setMaxLengthForInput(view, R.id.editCreateRecordTitle, MAX_TITLE_LENGTH);
        createViewModel.setMaxLengthForInput(view, R.id.editCreateRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.createRecordStatus);

        setCategoriesToDropdownButton(view);

        setOnClickToSaveButton(view);
        return view;
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        Button dropdownButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);

        dropdownButton.setText(DEFAULT_CATEGORY_TEXT);

        ArrayList<Category> categories = new ArrayList<>(sharedDataViewModel.getAllCategories());
        categories.add(0, new Category(DEFAULT_CATEGORY_TEXT));

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
                selectedCategoryId = which - 1;
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        Button button = view.findViewById(R.id.saveNewRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewRecord(view);
            }
        });
    }

    // Обробка створення нового запису
    private void getNewRecord(View view) {
        EditText textInput = view.findViewById(R.id.editCreateRecordText);
        EditText titleInput = view.findViewById(R.id.editCreateRecordTitle);
        String recordTitle = titleInput.getText().toString();
        if (recordTitle.length() != 0) {
            if (sharedDataViewModel.checkRecordTitleUnique(recordTitle)) {
                textViewStatus.setText("");
                sharedDataViewModel.addRecord(recordTitle, textInput.getText().toString(), selectedCategoryId);
                Toast.makeText(getActivity(), "Створено запис " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з таким заголовком вже існує");
            }
        } else {
            textViewStatus.setText("Заголовок запису не може бути порожнім");
        }
    }
}