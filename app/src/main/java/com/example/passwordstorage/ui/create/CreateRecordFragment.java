package com.example.passwordstorage.ui.create;

import static com.example.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TITLE_LENGTH;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.ui.HomeActivity;
import com.example.passwordstorage.ui.HomeViewModel;

import java.util.ArrayList;


public class CreateRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private HomeViewModel homeViewModel;

    private TextView textViewStatus;

    private int tempIconId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.setMaxLengthForInput(view, R.id.editCreateRecordTitle, MAX_TITLE_LENGTH);
        homeViewModel.setMaxLengthForInput(view, R.id.editCreateRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.createRecordStatus);

        setCategoriesToDropdownButton(view);

        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        return view;
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        Button dropdownButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);

        dropdownButton.setText(homeViewModel.setEmptyCategoryText());

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
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle)) {
                textViewStatus.setText("");
                Button categoryButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(categoryButton.getText().toString());
                sharedRecordsDataViewModel.addRecord(recordTitle, textInput.getText().toString(), category_id, tempIconId);
                Toast.makeText(getActivity(), "Створено запис " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з таким заголовком вже існує");
            }
        } else {
            textViewStatus.setText("Заголовок запису не може бути порожнім");
        }
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.createRecordIcon);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).showIconSelectionDialog(requireContext(), iconResourceId -> {
                    tempIconId = iconResourceId;
                    imageView.setImageResource(iconResourceId);
                });
            }
        });
    }
}